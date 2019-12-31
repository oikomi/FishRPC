package org.miaohong.fishrpc.core.rpc.chain.filter;


import org.miaohong.fishrpc.core.rpc.chain.FilterInvoke;
import org.miaohong.fishrpc.core.rpc.chain.FilterOrder;
import org.miaohong.fishrpc.core.rpc.chain.FilterType;
import org.miaohong.fishrpc.core.rpc.contex.RpcContex;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractFilter.class);

    protected FilterOrder filterOrder;

    protected FilterType filterType;

    protected abstract void processBusiness(FilterInvoke invoke, RpcRequest request);

    @Override
    public Object invoke(FilterInvoke invoke, RpcRequest request) {
        LOG.info("comsumer filter {} start", filterOrder);

        RpcContex context = RpcContex.getContext();

        processBusiness(invoke, request);

        if (filterOrder.isEnd()) {
            return context.getResult();
        }

        return invoke.invoke(request);
    }

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
