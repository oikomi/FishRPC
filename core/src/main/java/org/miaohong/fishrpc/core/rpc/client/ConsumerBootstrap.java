package org.miaohong.fishrpc.core.rpc.client;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.miaohong.fishrpc.core.extension.ExtensionLoader;
import org.miaohong.fishrpc.core.rpc.client.proxy.ProxyConstants;
import org.miaohong.fishrpc.core.rpc.client.strategy.ServiceStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConsumerBootstrap<T> extends AbstractConsumerBootstrap<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerBootstrap.class);

    private ServiceStrategy serviceStrategy = ExtensionLoader.getExtensionLoader(ServiceStrategy.class).
            getExtension(ServiceStrategy.class, this.consumerConfig.getStrategy());

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
        proxyInstance = proxyFactory.getProxy(consumerConfig.getProxyClass(), serviceStrategy);

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConsumerBootstrap<?> that = (ConsumerBootstrap<?>) o;
        return Objects.equal(serviceStrategy, that.serviceStrategy);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serviceStrategy);
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
