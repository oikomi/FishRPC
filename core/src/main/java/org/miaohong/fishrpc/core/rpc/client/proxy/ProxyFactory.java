package org.miaohong.fishrpc.core.rpc.client.proxy;


import org.miaohong.fishrpc.core.annotation.Scope;
import org.miaohong.fishrpc.core.annotation.Spi;
import org.miaohong.fishrpc.core.rpc.client.strategy.ServiceStrategy;


@Spi(scope = Scope.SINGLETON)
public interface ProxyFactory {
    <T> T getProxy(Class<T> clz, ServiceStrategy serviceStrategy);
}
