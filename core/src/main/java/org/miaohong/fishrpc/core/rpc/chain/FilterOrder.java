package org.miaohong.fishrpc.core.rpc.chain;

public enum FilterOrder {

    MESSAGE(1001), LB(2001), TRACE(3001);

    private int order;

    FilterOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }


    public boolean isEnd() {
        return this == MESSAGE;
    }
}
