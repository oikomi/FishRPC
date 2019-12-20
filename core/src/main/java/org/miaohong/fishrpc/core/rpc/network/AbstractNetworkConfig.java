package org.miaohong.fishrpc.core.rpc.network;

import org.miaohong.fishrpc.core.util.HardwareUtils;

public abstract class AbstractNetworkConfig implements NetworkConfig {

    protected final int serverNumThreads = Math.min(HardwareUtils.getNumberCPUCores() + 1, 32);
    protected String serverName;
    protected String host;
    protected int port;


    @Override
    public String getServerAddr() {
        return host + ":" + port;
    }

}
