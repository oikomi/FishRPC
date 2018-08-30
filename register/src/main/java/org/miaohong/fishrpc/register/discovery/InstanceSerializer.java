package org.miaohong.fishrpc.register.discovery;

/**
 * Injectable serializer for service instances
 */
public interface InstanceSerializer<T> {
    /**
     * Serialize an instance into bytes
     *
     * @param instance the instance
     * @return byte array representing the instance
     * @throws Exception any errors
     */
    byte[] serialize(ServiceInstance<T> instance) throws Exception;

    /**
     * Deserialize a byte array into an instance
     *
     * @param bytes the bytes
     * @return service instance
     * @throws Exception any errors
     */
    ServiceInstance<T> deserialize(byte[] bytes) throws Exception;
}
