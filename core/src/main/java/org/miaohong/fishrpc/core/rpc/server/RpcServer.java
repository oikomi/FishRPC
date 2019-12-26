package org.miaohong.fishrpc.core.rpc.server;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.Getter;
import org.miaohong.fishrpc.core.execption.CoreErrorConstant;
import org.miaohong.fishrpc.core.execption.ServerCoreException;
import org.miaohong.fishrpc.core.metrics.MetricSystem;
import org.miaohong.fishrpc.core.rpc.network.server.config.ServerConfig;
import org.miaohong.fishrpc.core.rpc.network.server.transport.NettyServer;
import org.miaohong.fishrpc.core.runtime.RuntimeContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RpcServer extends Server {

    private static final Logger LOG = LoggerFactory.getLogger(RpcServer.class);

    private NettyServer nettyServer;

    private MetricSystem metricSystem = MetricSystem.get();

    @Getter
    private volatile ServerState serverState = ServerState.INIT;

    public RpcServer(ServerConfig serverConfig) {
        try {
            RuntimeContext.now();
            nettyServer = new NettyServer(serverConfig);
            serverState = ServerState.ALIVE;
            RuntimeContext.cacheRpcServer(this);
        } catch (Exception e) {
            LOG.error("RPCServer init failed {}", e.getMessage(), e);
            throw new ServerCoreException(e.getMessage(), CoreErrorConstant.SERVER_DEFAULT_ERROR);
        } finally {
            serverState = ServerState.CLOSE;
        }
    }

    @Override
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public void start() {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(serverName), "server name is null");
        Preconditions.checkNotNull(nettyServer);
        metricSystem.start();
        nettyServer.start();
    }

    @Override
    public void shutDown() {
        nettyServer.shutdown();
    }

    @Override
    public void destroy() {
        LOG.info("[LifeCycle] {} destroy", RpcServer.class.getName());
        shutDown();
    }

    @Override
    public void destroy(DestroyHook hook) {
        if (hook != null) {
            hook.preDestroy();
        }
        destroy();
        if (hook != null) {
            hook.postDestroy();
        }
    }
}
