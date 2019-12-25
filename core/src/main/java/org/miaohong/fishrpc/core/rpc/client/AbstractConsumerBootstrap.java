package org.miaohong.fishrpc.core.rpc.client;

import com.google.common.base.Preconditions;
import org.miaohong.fishrpc.core.extension.ExtensionLoader;
import org.miaohong.fishrpc.core.rpc.base.Destroyable;
import org.miaohong.fishrpc.core.rpc.client.proxy.ProxyFactory;
import org.miaohong.fishrpc.core.rpc.register.Register;
import org.miaohong.fishrpc.core.rpc.register.RegisterRole;

public abstract class AbstractConsumerBootstrap<T> implements Destroyable, RegisterRole {

    protected T proxyInstance;
    protected ConsumerConfig<T> consumerConfig;
    protected ProxyFactory proxyFactory;

    protected Register register;

    public AbstractConsumerBootstrap(ConsumerConfig<T> consumerConfig) {
        Preconditions.checkNotNull(consumerConfig);
        this.consumerConfig = consumerConfig;
        this.proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).
                getExtension(this.consumerConfig.getProxy());
        this.register = ExtensionLoader.getExtensionLoader(Register.class).
                getExtension(this.consumerConfig.getRegister());
    }

    public abstract void startRegister();

}
