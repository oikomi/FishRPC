package org.miaohong.fishrpc.core.rpc.eventbus.event;

import lombok.Getter;
import org.miaohong.fishrpc.core.rpc.network.NetworkConfig;

public class ServerStartedEvent implements Event {

    @Getter
    private NetworkConfig serverConfig;

    public ServerStartedEvent(NetworkConfig serverConfig) {
        this.serverConfig = serverConfig;
    }
}
