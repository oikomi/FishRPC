package org.miaohong.fishrpc.core.util;

import lombok.Getter;

public enum SnowFlakeUniq {

    REQ_ID(1, 2), SERVICE(3, 4);

    @Getter
    private long datacenterId;

    @Getter
    private long machineId;


    SnowFlakeUniq(long datacenterId, long machineId) {
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }
}
