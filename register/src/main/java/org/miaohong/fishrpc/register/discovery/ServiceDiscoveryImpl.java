package org.miaohong.fishrpc.register.discovery;

import org.apache.curator.framework.CuratorFramework;

import java.io.IOException;

public class ServiceDiscoveryImpl<T> implements ServiceDiscovery<T> {

    private final CuratorFramework client;

    public ServiceDiscoveryImpl(CuratorFramework client) {
        this.client = client;
    }


    @Override
    public void start() throws Exception {

    }

    @Override
    public void registerService(ServiceInstance<T> service) throws Exception {

    }

    @Override
    public void updateService(ServiceInstance<T> service) throws Exception {

    }

    @Override
    public void unregisterService(ServiceInstance<T> service) throws Exception {

    }

    @Override
    public void close() throws IOException {

    }
}
