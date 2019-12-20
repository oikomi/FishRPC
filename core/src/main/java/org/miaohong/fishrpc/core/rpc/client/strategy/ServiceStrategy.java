package org.miaohong.fishrpc.core.rpc.client.strategy;

import org.miaohong.fishrpc.core.rpc.network.client.transport.NettyClientHandler;
import org.miaohong.fishrpc.core.rpc.register.serializer.ServiceInstance;

public interface ServiceStrategy {

    ServiceInstance getInstance(int timeout);

    ServiceInstance getInstance();

    NettyClientHandler getNettyClientHandler(String serverAddr);
}
