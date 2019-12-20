package org.miaohong.fishrpc.core.rpc.network;

public interface NetworkConfig {

    String getHost();

    int getPort();

    String getServerAddr();

    int getThreadsNum();
}
