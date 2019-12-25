package org.miaohong.fishrpc.core.runtime;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.miaohong.fishrpc.core.rpc.base.Destroyable;
import org.miaohong.fishrpc.core.rpc.client.ConsumerBootstrap;
import org.miaohong.fishrpc.core.rpc.client.RpcClient;
import org.miaohong.fishrpc.core.rpc.server.RpcServer;
import org.miaohong.fishrpc.core.rpc.service.ServiceBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class RuntimeContext {

    /**
     * 当前进程Id
     */
    public static final String PID = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    /**
     * 当前应用启动时间（用这个类加载时间为准）
     */
    public static final long START_TIME = now();
    private static final Logger LOG = LoggerFactory.getLogger(RuntimeContext.class);
    private static final ConcurrentMap<String, Object> CONTEXT = Maps.newConcurrentMap();

    private static final Set<ServiceBootstrap> EXPORTED_SERVICE_CONFIGS = Sets.newConcurrentHashSet();

    private static final Set<ConsumerBootstrap> REFERRED_CONSUMER_CONFIGS = Sets.newConcurrentHashSet();

    private static final Set<RpcServer> RPC_SERVERS = Sets.newConcurrentHashSet();

    private static final Set<RpcClient> RPC_CLIENTS = Sets.newConcurrentHashSet();

    private static final List<Destroyable.DestroyHook> DESTROY_HOOKS = new CopyOnWriteArrayList<>();

    static {
        if (LOG.isInfoEnabled()) {
            LOG.info("FISH RPC Framework , PID is:{}", PID);
        }
        // 初始化一些上下文
        initContext();
        // 初始化其它模块
        // 增加jvm关闭事件
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (LOG.isWarnEnabled()) {
                LOG.warn("RPC Framework catch JVM shutdown event, Run shutdown hook now.");
            }
            destroy();
        }, "RPC-ShutdownHook"));

    }

    /**
     * 初始化一些上下文
     */
    private static void initContext() {
    }

    private static void destroy() {
        for (Destroyable.DestroyHook destroyHook : DESTROY_HOOKS) {
            destroyHook.preDestroy();
        }
        for (ServiceBootstrap bootstrap : EXPORTED_SERVICE_CONFIGS) {
            bootstrap.unExport();
            bootstrap.destroy();
        }
        // 关闭调用的服务
        for (ConsumerBootstrap bootstrap : REFERRED_CONSUMER_CONFIGS) {
            bootstrap.destroy();
        }

        for (RpcServer rpcServer : RPC_SERVERS) {
            rpcServer.destroy();
        }

        for (RpcClient rpcClient : RPC_CLIENTS) {
            rpcClient.destroy();
        }
    }

    public static void registryDestroyHook(Destroyable.DestroyHook destroyHook) {
        DESTROY_HOOKS.add(destroyHook);
    }

    public static long now() {
        return System.currentTimeMillis();
    }

    public static void cacheConsumerBootstrap(ConsumerBootstrap consumerConfig) {
        REFERRED_CONSUMER_CONFIGS.add(consumerConfig);
    }

    public static void cacheServiceBootstrap(ServiceBootstrap serviceBootstrap) {
        EXPORTED_SERVICE_CONFIGS.add(serviceBootstrap);
    }

    public static void cacheRpcServer(RpcServer rpcServer) {
        RPC_SERVERS.add(rpcServer);
    }

    public static void cacheRpcClient(RpcClient rpcClient) {
        RPC_CLIENTS.add(rpcClient);
    }


    public static Object get(String key) {
        return CONTEXT.get(key);
    }

    public static Object putIfAbsent(String key, Object value) {
        return value == null ? CONTEXT.remove(key) : CONTEXT.putIfAbsent(key, value);
    }

    public static Object put(String key, Object value) {
        return value == null ? CONTEXT.remove(key) : CONTEXT.put(key, value);
    }

    public static ConcurrentMap<String, Object> getContext() {
        return new ConcurrentHashMap<>(CONTEXT);
    }

}
