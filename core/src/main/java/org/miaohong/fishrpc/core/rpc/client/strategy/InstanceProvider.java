package org.miaohong.fishrpc.core.rpc.client.strategy;

import org.miaohong.fishrpc.core.rpc.register.serializer.ServiceInstance;

import java.util.List;

public interface InstanceProvider {

    List<ServiceInstance> getInstances();

    List<ServiceInstance> getInstances(int timeout);
}
