package org.miaohong.fishrpc.core.rpc.chain;

public enum FilterType {

    CONSUMER(1), SERVICE(2);

    private int order;

    FilterType(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

}
