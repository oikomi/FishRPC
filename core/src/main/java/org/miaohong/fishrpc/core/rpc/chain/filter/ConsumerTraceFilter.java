package org.miaohong.fishrpc.core.rpc.chain.filter;

import lombok.ToString;
import org.miaohong.fishrpc.core.annotation.SpiMeta;
import org.miaohong.fishrpc.core.rpc.chain.FilterInvoke;
import org.miaohong.fishrpc.core.rpc.chain.FilterOrder;
import org.miaohong.fishrpc.core.rpc.chain.FilterType;
import org.miaohong.fishrpc.core.rpc.contex.RpcContex;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;
import org.miaohong.fishrpc.core.rpc.trace.TraceContex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpiMeta(name = "trace")
@ToString
public class ConsumerTraceFilter extends AbstractFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerTraceFilter.class);

    public ConsumerTraceFilter() {
        setFilterOrder(FilterOrder.TRACE);
        setFilterType(FilterType.CONSUMER);
    }

    @Override
    protected void processBusiness(FilterInvoke invoke, RpcRequest request) {

        RpcContex context = RpcContex.getContext();
        TraceContex traceContex = new TraceContex();
    }

}
