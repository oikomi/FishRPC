package org.miaohong.fishrpc.core.rpc.service;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import org.miaohong.fishrpc.core.rpc.base.Destroyable;
import org.miaohong.fishrpc.core.rpc.concurrency.NamedThreadFactory;
import org.miaohong.fishrpc.core.rpc.eventbus.event.ServerStartedEvent;
import org.miaohong.fishrpc.core.rpc.network.NetworkConfig;
import org.miaohong.fishrpc.core.rpc.register.AbstractRegister;
import org.miaohong.fishrpc.core.rpc.register.RegisterRole;
import org.miaohong.fishrpc.core.rpc.service.config.ServiceConfig;
import org.miaohong.fishrpc.core.runtime.RuntimeContext;
import org.miaohong.fishrpc.core.util.ThreadPoolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ServiceBootstrap<T> implements RegisterRole, Destroyable {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceBootstrap.class);
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition connected = lock.newCondition();
    private final ExecutorService executorService = ThreadPoolUtils.newFixedThreadPool(
            3, ThreadPoolUtils.buildQueue(3),
            new NamedThreadFactory("service register"));
    private AbstractRegister register;
    private ServiceConfig<T> serviceConfig;

    public ServiceBootstrap(AbstractRegister register, ServiceConfig<T> serviceConfig) {
        this.register = register;
        this.serviceConfig = serviceConfig;

        RuntimeContext.cacheServiceBootstrap(this);
    }

    private static void signalAvailable() {
        lock.lock();
        try {
            connected.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private void waitingForServerStarted() throws InterruptedException {
        lock.lock();
        try {
            connected.await();
        } finally {
            lock.unlock();
        }
    }

    private void initCheck() {
        Preconditions.checkNotNull(register);
        Preconditions.checkNotNull(serviceConfig);
    }

    public void export() {
        LOG.info("do service export");
        initCheck();
        executorService.submit(() -> {
            try {
                waitingForServerStarted();
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }

            serviceConfig.setServerConfig(ServiceBootstrapListener.serverConfig);
            LOG.info("start to export service");
            Preconditions.checkState(serviceConfig.getServerConfig() != null,
                    "server config can not empty");

            register.start(this);
            register.register(serviceConfig);
        });
    }

    public void unExport() {
    }

    @Override
    public void handleError(Exception exception) {

    }

    @Override
    public void destroy() {
        register.destroy();
    }

    @Override
    public void destroy(DestroyHook hook) {

    }

    public static class ServiceBootstrapListener {

        private static NetworkConfig serverConfig;

        @Subscribe
        public static void doAction(final Object event) {
            LOG.info("Received event [{}] and will take a action", event);
            if (event instanceof ServerStartedEvent) {
                LOG.info("server is started");
                ServerStartedEvent serverStartedEvent = (ServerStartedEvent) event;
                serverConfig = serverStartedEvent.getServerConfig();
                signalAvailable();
            }
        }
    }
}
