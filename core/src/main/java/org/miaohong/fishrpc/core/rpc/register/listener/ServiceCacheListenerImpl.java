package org.miaohong.fishrpc.core.rpc.register.listener;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.miaohong.fishrpc.core.execption.FrameworkException;
import org.miaohong.fishrpc.core.rpc.client.NettyClientFactory;
import org.miaohong.fishrpc.core.rpc.client.strategy.InstanceProvider;
import org.miaohong.fishrpc.core.rpc.eventbus.event.NettyClientHandlerRegistedEvent;
import org.miaohong.fishrpc.core.rpc.network.client.config.ClientConfig;
import org.miaohong.fishrpc.core.rpc.register.serializer.InstanceSerializer;
import org.miaohong.fishrpc.core.rpc.register.serializer.ServiceInstance;
import org.miaohong.fishrpc.core.util.ThreadPoolUtils;
import org.miaohong.fishrpc.core.util.concurrency.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static org.miaohong.fishrpc.core.execption.CoreErrorConstant.REGISTER_DEFAULT_ERROR;

public class ServiceCacheListenerImpl implements ServiceCacheListener, InstanceProvider {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceCacheListenerImpl.class);

    private static ConcurrentMap<String, ServiceInstance> instances = Maps.newConcurrentMap();

    private static ConcurrentMap<String, ServiceInstance> waitInstances = Maps.newConcurrentMap();

    private ThreadPoolExecutor threadExecutor = getExecutor();

    private ReentrantLock lock = new ReentrantLock();
    private Condition connected = lock.newCondition();

    private ServiceCacheListenerImpl() {
    }

    public static ServiceCacheListenerImpl get() {
        return ServiceCacheListenerImpl.SingletonHolder.INSTANCE;
    }

    private ThreadPoolExecutor getExecutor() {
        RejectedExecutionHandler handler = (Runnable r, ThreadPoolExecutor executor) -> {
            LOG.error("Task:{} has been reject because of threadPool exhausted!" +
                            " pool:{}, active:{}, queue:{}, taskcnt: {}", r,
                    executor.getPoolSize(),
                    executor.getActiveCount(),
                    executor.getQueue().size(),
                    executor.getTaskCount());
            throw new RejectedExecutionException("Callback handler thread pool has bean exhausted");
        };

        return ThreadPoolUtils.newCachedThreadPool(
                4,
                64,
                1000,
                ThreadPoolUtils.buildQueue(128),
                new NamedThreadFactory("client handler"), handler);
    }

    private void signalAvailableHandler() {
        lock.lock();
        try {
            connected.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private boolean waitingForHandler(long connectTimeoutMillis) throws InterruptedException {
        lock.lock();
        try {
            return connected.await(connectTimeoutMillis, TimeUnit.MILLISECONDS);
        } finally {
            lock.unlock();
        }
    }


    @Override
    public List<ServiceInstance> getInstances() {
        List<ServiceInstance> res = Lists.newArrayList();
        instances.forEach((k, v) -> {
            res.add(v);
        });

        return res;
    }

    @Override
    public List<ServiceInstance> getInstances(int timeout) {
        int size = instances.size();
        while (size <= 0) {
            try {
                boolean available = waitingForHandler(timeout);
                if (available) {
                    LOG.info("wait available");
                    size = instances.size();
                }
            } catch (InterruptedException e) {
                LOG.error("Waiting for available node is interrupted! {} ", e.getMessage(), e);
                throw new FrameworkException("Can't connect any servers!", REGISTER_DEFAULT_ERROR);
            }
        }

        List<ServiceInstance> res = Lists.newArrayList();
        instances.forEach((k, v) -> {
            res.add(v);
        });

        return res;
    }

    @Override
    public void onChange(ChildData data, String path, boolean add, InstanceSerializer serializer) {
        LOG.info("on data change {}", new String(data.getData()));

        String serverAddr = data.getPath().substring(path.length() + 1);
        if (StringUtils.isBlank(serverAddr)) {
            return;
        }
        LOG.info("instances : {}", instances);
        if (add) {
            try {
                String[] strs = serverAddr.split(":");
                threadExecutor.submit(NettyClientFactory.getClient(
                        new ClientConfig(strs[0], Integer.parseInt(strs[1]))));
                ServiceInstance serviceInstance = serializer.deserialize(data.getData());
                waitInstances.put(serverAddr, serviceInstance);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                throw new FrameworkException(e.getMessage(), REGISTER_DEFAULT_ERROR);
            }
        } else {
            instances.remove(serverAddr);
        }
    }

    @Subscribe
    public void doAction(final Object event) {
        LOG.info("Received event [{}] and will take a action", event);
        if (event instanceof NettyClientHandlerRegistedEvent) {
            NettyClientHandlerRegistedEvent rpcClientRegistedEvent = (NettyClientHandlerRegistedEvent) event;
            String serverAddr = rpcClientRegistedEvent.getServerAddr();
            instances.put(serverAddr, waitInstances.get(serverAddr));
            waitInstances.remove(serverAddr);
            signalAvailableHandler();
        }
    }

    private static class SingletonHolder {
        private static final ServiceCacheListenerImpl INSTANCE = new ServiceCacheListenerImpl();
    }

}
