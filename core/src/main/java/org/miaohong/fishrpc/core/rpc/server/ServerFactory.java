package org.miaohong.fishrpc.core.rpc.server;


import org.miaohong.fishrpc.core.rpc.network.server.config.ServerConfig;

public class ServerFactory {

    public static synchronized Server getServer(ServerConfig serverConfig) {
        try {
            return new RpcServer(serverConfig);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
