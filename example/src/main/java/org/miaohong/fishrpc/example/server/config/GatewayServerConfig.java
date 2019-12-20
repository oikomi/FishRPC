package org.miaohong.fishrpc.example.server.config;


import org.miaohong.fishrpc.core.conf.prop.BasePropConfig;

public class GatewayServerConfig extends BasePropConfig {

    private static final String SERVER_PROP_NAME = "server.properties";

    @Override
    protected String getPropertiesPath() {
        return SERVER_PROP_NAME;
    }
}
