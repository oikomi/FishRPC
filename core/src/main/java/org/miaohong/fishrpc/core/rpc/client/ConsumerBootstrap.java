package org.miaohong.fishrpc.core.rpc.client;

import com.google.common.base.Preconditions;
import org.miaohong.fishrpc.core.rpc.client.proxy.ProxyConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConsumerBootstrap<T> extends AbstractConsumerBootstrap<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerBootstrap.class);

    public ConsumerBootstrap(ConsumerConfig<T> consumerConfig) {
        super(consumerConfig);
        startRegister();
    }

    @Override
    public void startRegister() {
        register.start(this);
        register.subscribe(consumerConfig);
    }

    private boolean checkProxy() {
        return ProxyConstants.PROXY_JDK.equals(consumerConfig.getProxy())
                || ProxyConstants.PROXY_BYTEBUDDY.equals(consumerConfig.getProxy());
    }

    public T refer() {
        Preconditions.checkNotNull(register);
        if (proxyInstance != null) {
            return proxyInstance;
        }

        Preconditions.checkState(checkProxy(), "rpc client proxy must be jdk or bytebuddy");
        Preconditions.checkNotNull(proxyFactory);
        proxyInstance = proxyFactory.getProxy(consumerConfig.getProxyClass());

        return proxyInstance;
    }

    @Override
    public void handleError(Exception e) {
        LOG.error(e.getMessage(), e);
    }

    @Override
    public void destroy() {
        if (register != null) {
            register.destroy();
        }
    }

    @Override
    public void destroy(DestroyHook hook) {
        if (hook != null) {
            hook.preDestroy();
        }
        destroy();
        if (hook != null) {
            hook.postDestroy();
        }
    }

}
