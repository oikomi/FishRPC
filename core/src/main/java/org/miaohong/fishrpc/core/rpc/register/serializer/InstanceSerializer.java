package org.miaohong.fishrpc.core.rpc.register.serializer;

public interface InstanceSerializer {
    byte[] serialize(ServiceInstance instance) throws Exception;

    ServiceInstance deserialize(byte[] bytes) throws Exception;
}
