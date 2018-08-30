package org.miaohong.fishrpc.register.discovery;

import java.io.Closeable;

public interface ServiceDiscovery<T> extends Closeable {


    /**
     * The discovery must be started before use
     *
     * @throws Exception errors
     */
    void start() throws Exception;

    /**
     * Register/re-register a service
     *
     * @param service service to add
     * @throws Exception errors
     */
    void registerService(ServiceInstance<T> service) throws Exception;

    /**
     * Update a service
     *
     * @param service service to update
     * @throws Exception errors
     */
    void updateService(ServiceInstance<T> service) throws Exception;

    /**
     * Unregister/remove a service instance
     *
     * @param service the service
     * @throws Exception errors
     */
    void unregisterService(ServiceInstance<T> service) throws Exception;


}
