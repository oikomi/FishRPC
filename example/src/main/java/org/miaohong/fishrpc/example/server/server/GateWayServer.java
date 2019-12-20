package org.miaohong.fishrpc.example.server.server;


import org.miaohong.fishrpc.core.conf.prop.PropConfig;
import org.miaohong.fishrpc.core.rpc.network.server.config.ServerConfig;
import org.miaohong.fishrpc.core.rpc.service.config.ServiceConfig;
import org.miaohong.fishrpc.core.runtime.JvmShutdownSafeguard;
import org.miaohong.fishrpc.core.runtime.SignalHandler;
import org.miaohong.fishrpc.core.util.DateUtils;
import org.miaohong.fishrpc.example.proto.GatewayImpl;
import org.miaohong.fishrpc.example.proto.GatewayProto;
import org.miaohong.fishrpc.example.server.config.GatewayServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class GateWayServer {

    private static final Logger LOG = LoggerFactory.getLogger(GateWayServer.class);

    private static String buildServerName(String addr, int port) {
        return String.format("gateway-%s:%d-%s", addr, port, DateUtils.dateToStr(new Date()));
    }

    public static void main(String[] args) {

        SignalHandler.register(LOG);
        JvmShutdownSafeguard.installAsShutdownHook(LOG);

        PropConfig propConfig = new GatewayServerConfig();

        ServiceConfig<GatewayProto> serviceConfig = new ServiceConfig<>()
                .setInterfaceId(GatewayProto.class.getName())
                .setRef(new GatewayImpl());

        serviceConfig.export();

        ServerConfig serverConfig = new ServerConfig()
                .setServerName(buildServerName(propConfig.getString("server.bind.addr"),
                        propConfig.getInt("server.bind.port", 15000)))
                .setHost(propConfig.getString("server.bind.addr"))
                .setPort(propConfig.getInt("server.bind.port", 15000))
                .buildIfAbsent();
    }
}
