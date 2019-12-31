package org.miaohong.fishrpc.core.rpc.client.proxy.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.miaohong.fishrpc.core.annotation.SpiMeta;
import org.miaohong.fishrpc.core.execption.ClientCoreException;
import org.miaohong.fishrpc.core.rpc.client.proxy.ProxyConstants;
import org.miaohong.fishrpc.core.rpc.client.proxy.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpiMeta(name = ProxyConstants.PROXY_BYTEBUDDY)
public class BytebuddyProxyFactory implements ProxyFactory {

    private static final Logger LOG = LoggerFactory.getLogger(BytebuddyProxyFactory.class);

    private static final Map<Class, Class> PROXY_CLASS_MAP = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clz) {
        Class<? extends T> cls = PROXY_CLASS_MAP.get(clz);
        if (cls == null) {
            cls = new ByteBuddy()
                    .subclass(clz)
                    .method(ElementMatchers.isDeclaredBy(clz).or(ElementMatchers.isEquals())
                            .or(ElementMatchers.isToString().or(ElementMatchers.isHashCode())))
                    .intercept(MethodDelegation.to(new BytebuddyInvocationHandler(), "handler"))
                    .make()
                    .load(clz.getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                    .getLoaded();

            PROXY_CLASS_MAP.put(clz, cls);
        }
        try {
            return cls.newInstance();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new ClientCoreException("construct proxy with bytebuddy occurs error", e);
        }
    }
}
