package org.miaohong.fishrpc.core.rpc.chain;

import lombok.ToString;
import org.miaohong.fishrpc.core.execption.FrameworkException;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ToString
public class FilterInvoke {
    private static final Logger LOG = LoggerFactory.getLogger(FilterInvoke.class);

    public Filter next;
    public FilterInvoke invoke;

    public FilterInvoke(Filter filter, FilterInvoke invoke) {
        this.next = filter;
        this.invoke = invoke;
    }

    public Object invoke(RpcRequest request) {
        if (invoke == null && next == null) {
            throw new FrameworkException();
        }

        LOG.info("invoke {}", invoke);
        LOG.info("next {}", next);

        return next == null ?
                invoke.invoke(request) :
                next.invoke(invoke, request);
    }
}
