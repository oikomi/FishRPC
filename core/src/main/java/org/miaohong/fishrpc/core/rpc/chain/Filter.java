package org.miaohong.fishrpc.core.rpc.chain;

import org.miaohong.fishrpc.core.annotation.Spi;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;


@Spi
public interface Filter extends Comparable<Filter> {

    Object invoke(FilterInvoke invoke, RpcRequest request);

    FilterOrder getFilterOrder();

    void setFilterOrder(FilterOrder filterOrder);

    FilterType getFilterType();

    void setFilterType(FilterType filterType);

}
