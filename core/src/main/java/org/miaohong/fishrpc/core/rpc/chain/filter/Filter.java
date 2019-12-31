package org.miaohong.fishrpc.core.rpc.chain.filter;

import org.miaohong.fishrpc.core.annotation.Spi;
import org.miaohong.fishrpc.core.rpc.chain.FilterInvoke;
import org.miaohong.fishrpc.core.rpc.chain.FilterOrder;
import org.miaohong.fishrpc.core.rpc.chain.FilterType;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;


@Spi
public interface Filter extends Comparable<Filter> {

    Object invoke(FilterInvoke invoke, RpcRequest request);

    FilterOrder getFilterOrder();

    void setFilterOrder(FilterOrder filterOrder);

    FilterType getFilterType();

    void setFilterType(FilterType filterType);

}
