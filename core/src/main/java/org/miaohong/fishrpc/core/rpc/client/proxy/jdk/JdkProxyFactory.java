package org.miaohong.fishrpc.core.rpc.client.proxy.jdk;


import org.miaohong.fishrpc.core.annotation.SpiMeta;
import org.miaohong.fishrpc.core.rpc.client.proxy.ProxyConstants;
import org.miaohong.fishrpc.core.rpc.client.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

@SpiMeta(name = ProxyConstants.PROXY_JDK)
public class JdkProxyFactory implements ProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clz) {
        return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new JDKInvocationHandler<>());
    }
}
