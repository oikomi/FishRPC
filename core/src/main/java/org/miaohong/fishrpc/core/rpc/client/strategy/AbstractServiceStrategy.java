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

    private static final Logger LOG = LoggerFactory.getLogger(AbstractServiceStrategy.class);

    protected static ConcurrentMap<String, NettyClientHandler> nettyClientHandlers = Maps.newConcurrentMap();

    protected InstanceProvider instanceProvider = ServiceCacheListenerImpl.get();

    public AbstractServiceStrategy() {
    }

    public static class RpcClientHandlerListener {

        @Subscribe
        public void doAction(final Object event) {
            LOG.info("Received event [{}] and will take a action", event);
            if (event instanceof NettyClientHandlerRegistedEvent) {
                NettyClientHandlerRegistedEvent rpcClientRegistedEvent = (NettyClientHandlerRegistedEvent) event;
                nettyClientHandlers.put(rpcClientRegistedEvent.getServerAddr(), rpcClientRegistedEvent.getNettyClientHandler());
                LOG.info("nettyClientHandlers {} ", nettyClientHandlers);
            }
        }
    }

}
