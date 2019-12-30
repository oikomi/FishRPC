package org.miaohong.fishrpc.core.rpc.chain;


public abstract class AbstractFilter implements Filter {

    protected FilterOrder filterOrder;

    protected FilterType filterType;

    @Override
    public FilterType getFilterType() {
        return filterType;
    }

    @Override
    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    @Override
    public FilterOrder getFilterOrder() {
        return filterOrder;
    }

    @Override
    public void setFilterOrder(FilterOrder filterOrder) {
        this.filterOrder = filterOrder;
    }

    @Override
    public int compareTo(Filter o) {
        return this.getFilterOrder().compareTo(o.getFilterOrder());
    }
}
