package org.miaohong.fishrpc.register.discovery;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;


public class ServiceDiscoveryImpl<T> implements ServiceDiscovery<T> {

    private final static Logger log = LoggerFactory.getLogger(ServiceDiscoveryImpl.class);
    private final CuratorFramework client;
    private final ConcurrentMap<String, Entry<T>> services = Maps.newConcurrentMap();
    private final InstanceSerializer<T> serializer;
    private final String basePath;

    private final ConnectionStateListener connectionStateListener = (client, newState) -> {
        if ((newState == ConnectionState.RECONNECTED) || (newState == ConnectionState.CONNECTED)) {
            try {
                log.debug("Re-registering due to reconnection");
                reRegisterServices();

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("Could not re-register instances after reconnection", e);
            }
        }
    };


    private void reRegisterServices() throws Exception {
        for (final Entry<T> entry : services.values()) {
            synchronized (entry) {
                internalRegisterService(entry.service);
            }
        }
    }

    String pathForName(String name)
    {
        return ZKPaths.makePath(basePath, name);
    }

    @VisibleForTesting
    String pathForInstance(String name, String id)
    {
        return ZKPaths.makePath(pathForName(name), id);
    }

    @VisibleForTesting
    protected void internalRegisterService(ServiceInstance<T> service) throws Exception {
        byte[] bytes = serializer.serialize(service);
        String path = pathForInstance(service.getName(), service.getId());

        final int MAX_TRIES = 2;
        boolean isDone = false;
        for (int i = 0; !isDone && (i < MAX_TRIES); ++i) {
            try {
                CreateMode mode;
                switch (service.getServiceType()) {
                    case DYNAMIC:
                        mode = CreateMode.EPHEMERAL;
                        break;
                    case DYNAMIC_SEQUENTIAL:
                        mode = CreateMode.EPHEMERAL_SEQUENTIAL;
                        break;
                    default:
                        mode = CreateMode.PERSISTENT;
                        break;
                }
                client.create().creatingParentContainersIfNeeded().withMode(mode).forPath(path, bytes);
                isDone = true;
            } catch (KeeperException.NodeExistsException e) {
                client.delete().forPath(path);  // must delete then re-create so that watchers fire
            }
        }
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


    private static class Entry<T>
    {
        private volatile ServiceInstance<T> service;
        private volatile NodeCache cache;

        private Entry(ServiceInstance<T> service)
        {
            this.service = service;
        }
    }

}
