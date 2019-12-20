package org.miaohong.fishrpc.core.rpc.service;

public enum ServiceType {

    DYNAMIC,
    STATIC,
    PERMANENT,
    DYNAMIC_SEQUENTIAL;

    public boolean isDynamic() {
        return this == DYNAMIC || this == DYNAMIC_SEQUENTIAL;
    }

}
