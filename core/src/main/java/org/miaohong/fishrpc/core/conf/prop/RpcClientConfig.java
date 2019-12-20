package org.miaohong.fishrpc.core.conf.prop;

public class RpcClientConfig extends BasePropConfig {

    private static final String RPC_CLIENT_PROP_NAME = "config/rpcclient.properties";

    private RpcClientConfig() {
    }

    public static RpcClientConfig get() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    protected String getPropertiesPath() {
        return RPC_CLIENT_PROP_NAME;
    }

    public String getRpcClientProxy() {
        return getString("rpc.client.proxy");
    }

    private static class SingletonHolder {
        private static final RpcClientConfig INSTANCE = new RpcClientConfig();
    }

}
