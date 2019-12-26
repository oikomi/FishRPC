package org.miaohong.fishrpc.core.rpc.register.zk;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.UnhandledErrorListener;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;
import org.miaohong.fishrpc.core.annotation.SpiMeta;
import org.miaohong.fishrpc.core.execption.FrameworkException;
import org.miaohong.fishrpc.core.execption.SystemCoreException;
import org.miaohong.fishrpc.core.rpc.client.ConsumerConfig;
import org.miaohong.fishrpc.core.rpc.eventbus.event.EventAction;
import org.miaohong.fishrpc.core.rpc.eventbus.event.ServiceRegistedEvent;
import org.miaohong.fishrpc.core.rpc.network.NetworkConfig;
import org.miaohong.fishrpc.core.rpc.network.server.config.ServerConfig;
import org.miaohong.fishrpc.core.rpc.register.AbstractRegister;
import org.miaohong.fishrpc.core.rpc.register.RegisterConstants;
import org.miaohong.fishrpc.core.rpc.register.RegisterPropConfig;
import org.miaohong.fishrpc.core.rpc.register.RegisterRole;
import org.miaohong.fishrpc.core.rpc.register.listener.ServiceCacheListenerImpl;
import org.miaohong.fishrpc.core.rpc.register.serializer.JsonInstanceSerializer;
import org.miaohong.fishrpc.core.rpc.register.serializer.ServiceInstance;
import org.miaohong.fishrpc.core.rpc.service.config.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;


@SpiMeta(name = RegisterConstants.REGISTER_ZOOKEEPER)
public class ZookeeperRegistry extends AbstractRegister implements UnhandledErrorListener {

    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperRegistry.class);
    private static final String CONTEXT_SEP = "/";
    private static final ConcurrentMap<ConsumerConfig, PathChildrenCache>
            INTERFACE_SERVICE_CACHE = Maps.newConcurrentMap();

    private static final Set<ServiceConfig> SERVICE_CONFIG_LIST = Sets.newConcurrentHashSet();
    private CuratorFramework zkClient;

    private JsonInstanceSerializer serializer = JsonInstanceSerializer.get();

    public ZookeeperRegistry() {
        super(RegisterPropConfig.get());
    }

    private void buildZkClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(
                registerPropConfig.getRetryWait(), registerPropConfig.getMaxRetryAttempts());
        CuratorFrameworkFactory.Builder zkClientuilder = CuratorFrameworkFactory.builder()
                .connectString(registerPropConfig.getAddress())
                .sessionTimeoutMs(registerPropConfig.getSessionTimeout())
                .connectionTimeoutMs(registerPropConfig.getTimeout())
                .canBeReadOnly(false)
                .retryPolicy(retryPolicy)
                .defaultData(null);

        zkClient = zkClientuilder.build();
    }

    private synchronized void init() {
        Preconditions.checkState(zkClient == null, "zk client already init");

        buildZkClient();
        zkClient.getUnhandledErrorListenable().addListener(this);
        zkClient.getConnectionStateListenable().addListener(
                (client, newState) -> handleConnectionStateChange(newState));
    }

    private void handleConnectionStateChange(ConnectionState newState) {
        switch (newState) {
            case CONNECTED:
                LOG.info("Connected to ZooKeeper quorum.");
                break;
            case SUSPENDED:
                LOG.info("Connection to ZooKeeper suspended.");
                break;
            case RECONNECTED:
                LOG.info("Connection to ZooKeeper was reconnected.");
                //// recoverRegistryData();
                break;
            case LOST:
                // Maybe we have to throw an exception here to terminate
                LOG.info("Connection to ZooKeeper lost.");
                break;
            case READ_ONLY:
                LOG.info("Connection to ZooKeeper read only.");
                break;
            default:
                break;
        }
    }

    @Override
    public void start(RegisterRole registerRole) {
        this.registerRole = registerRole;
        init();
        Preconditions.checkNotNull(zkClient, "Start zookeeper registry must be do init first!");
        if (zkClient.getState() == CuratorFrameworkState.STARTED) {
            LOG.info("zookeeper client already started");
            return;
        }
        try {
            zkClient.start();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new FrameworkException("Failed to start zookeeper zkClient", e);
        }
        Preconditions.checkState(zkClient.getState() == CuratorFrameworkState.STARTED,
                "zookeeper registry not started!");
    }

    private String buildServicePath(String rootPath, ServiceConfig config) {
        return rootPath + config.getInterfaceId() + "/services";
    }

    private String buildServicePath(String rootPath, ConsumerConfig config) {
        return rootPath + config.getInterfaceId() + "/services";
    }


    @Override
    public void register(final ServiceConfig serviceConfig) {
        LOG.info("do register");
        NetworkConfig serverConfig = serviceConfig.getServerConfig();
        StringBuilder sb = new StringBuilder();
        String serverUrl = sb.append(buildServicePath(registerPropConfig.getRoot(), serviceConfig)).
                append(CONTEXT_SEP).append(serverConfig.getHost()).append(":")
                .append(serverConfig.getPort()).toString();

        try {
            getAndCheckZkClient().create().creatingParentsIfNeeded()
                    .withMode(getCreateMode(serviceConfig))
                    .forPath(serverUrl, serializer.serialize(
                            ServiceInstance.<ServerConfig>builder(serviceConfig).build()));

            SERVICE_CONFIG_LIST.add(serviceConfig);
            LOG.info("start send event");
            serviceConfig.getEventBus().post(new ServiceRegistedEvent(EventAction.ADD,
                    serviceConfig.getInterfaceId(), serviceConfig.getRef()));
        } catch (KeeperException.NodeExistsException ignored) {
            SERVICE_CONFIG_LIST.add(serviceConfig);
            serviceConfig.getEventBus().post(new ServiceRegistedEvent(EventAction.ADD,
                    serviceConfig.getInterfaceId(), serviceConfig.getRef()));
            LOG.warn("service has exists in zookeeper, service={}", serverUrl);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new FrameworkException(e.getMessage());
        }
    }

    @Override
    public void unRegister(final ServiceConfig serviceConfig) {
        LOG.info("do unRegister");
        NetworkConfig serverConfig = serviceConfig.getServerConfig();
        StringBuilder sb = new StringBuilder();
        String serverUrl = sb.append(buildServicePath(registerPropConfig.getRoot(), serviceConfig)).
                append(CONTEXT_SEP).append(serverConfig.getHost()).append(":")
                .append(serverConfig.getPort()).toString();

        try {
            getAndCheckZkClient().delete().deletingChildrenIfNeeded().forPath(serverUrl);
            SERVICE_CONFIG_LIST.remove(serviceConfig);
            serviceConfig.getEventBus().post(new ServiceRegistedEvent(EventAction.DEL,
                    serviceConfig.getInterfaceId(), serviceConfig.getRef()));
        } catch (KeeperException.NodeExistsException ignored) {
            LOG.warn("service has exists in zookeeper, service={}", serverUrl);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new FrameworkException(e.getMessage());
        }

    }

    @Override
    public void subscribe(final ConsumerConfig config) {
        LOG.info("do subscribe");
        final String servicePath = buildServicePath(registerPropConfig.getRoot(), config);
        PathChildrenCache pathChildrenCache = INTERFACE_SERVICE_CACHE.get(config);
        if (pathChildrenCache == null) {
            pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
            ZkServiceCache zkServiceCache = new ZkServiceCache(servicePath);
            zkServiceCache.addListener(ServiceCacheListenerImpl.get());
            pathChildrenCache.getListenable().addListener(zkServiceCache);
            try {
                pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                throw new SystemCoreException(e.getMessage());
            }
            INTERFACE_SERVICE_CACHE.put(config, pathChildrenCache);
        }
    }

    @Override
    public void unSubscribe() {
        INTERFACE_SERVICE_CACHE.forEach((k, v) -> {
            try {
                v.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        });
    }

    private CuratorFramework getAndCheckZkClient() {
        if (zkClient == null || zkClient.getState() != CuratorFrameworkState.STARTED) {
            throw new SystemCoreException("Zookeeper client is not available");
        }
        return zkClient;
    }


    private void closePathChildrenCache() {
        for (Map.Entry<ConsumerConfig, PathChildrenCache> entry : INTERFACE_SERVICE_CACHE.entrySet()) {
            try {
                entry.getValue().close();
            } catch (Exception e) {
                LOG.error("Close PathChildrenCache error!", e);
                throw new FrameworkException(e.getMessage());
            }
        }
    }

    @Override
    public void destroy() {
        LOG.info("register do destory");
        if (!SERVICE_CONFIG_LIST.isEmpty()) {
            SERVICE_CONFIG_LIST.forEach(this::unRegister);
        }

        closePathChildrenCache();
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            zkClient.close();
        }
    }

    @Override
    public void destroy(DestroyHook hook) {
        hook.preDestroy();
        destroy();
        hook.postDestroy();
    }

    @Override
    public void unhandledError(String message, Throwable e) {
        registerRole.handleError(new SystemCoreException("Unhandled error : " + message, e));
    }
}
