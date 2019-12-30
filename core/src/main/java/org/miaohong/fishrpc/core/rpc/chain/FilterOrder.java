package org.miaohong.fishrpc.core.rpc.chain;

public enum FilterOrder {

    MESSAGE(1001), TRACE(2001), SSL(3001);

    private int order;

    FilterOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

}
