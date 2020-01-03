package org.miaohong.fishrpc.core.rpc.client.strategy;

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import org.miaohong.fishrpc.core.rpc.eventbus.event.NettyClientHandlerRegistedEvent;
import org.miaohong.fishrpc.core.rpc.network.client.transport.NettyClientHandler;
import org.miaohong.fishrpc.core.rpc.register.listener.ServiceCacheListenerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;

public abstract class AbstractServiceStrategy implements ServiceStrategy {

    protected static final ConcurrentMap<String, NettyClientHandler> NETTY_CLIENT_HANDLER_CONCURRENT_MAP = Maps.newConcurrentMap();
    private static final Logger LOG = LoggerFactory.getLogger(AbstractServiceStrategy.class);
    protected InstanceProvider instanceProvider = ServiceCacheListenerImpl.get();

    public AbstractServiceStrategy() {
    }

    public static class RpcClientHandlerListener {

        @Subscribe
        public void doAction(final Object event) {
            LOG.info("Received event [{}] and will take a action", event);
            if (event instanceof NettyClientHandlerRegistedEvent) {
                NettyClientHandlerRegistedEvent rpcClientRegistedEvent = (NettyClientHandlerRegistedEvent) event;
                NETTY_CLIENT_HANDLER_CONCURRENT_MAP.put(rpcClientRegistedEvent.getServerAddr(), rpcClientRegistedEvent.getNettyClientHandler());
                LOG.info("NETTY_CLIENT_HANDLER_CONCURRENT_MAP {} ", NETTY_CLIENT_HANDLER_CONCURRENT_MAP);
            }
        }
    }

}
