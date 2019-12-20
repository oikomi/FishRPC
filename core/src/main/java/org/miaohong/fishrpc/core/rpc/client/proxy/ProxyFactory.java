package org.miaohong.fishrpc.core.rpc.client.proxy;


import org.miaohong.fishrpc.core.rpc.client.strategy.ServiceStrategy;

public interface ProxyFactory {
    <T> T getProxy(Class<T> clz, ServiceStrategy serviceStrategy);
}
