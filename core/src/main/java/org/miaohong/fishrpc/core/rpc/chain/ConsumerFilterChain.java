package org.miaohong.fishrpc.core.rpc.chain;

import org.miaohong.fishrpc.core.execption.FrameworkException;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;
import org.miaohong.fishrpc.core.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerFilterChain extends FilterChain {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerFilterChain.class);

    private FilterInvoke head;

    public ConsumerFilterChain() {
        super();
        head = new FilterInvoke(null, null);
    }

    @Override
    public Object invoke(RpcRequest request) {
        if (CommonUtils.isEmpty(filters)) {
            throw new FrameworkException("filters can not be null");
        }

        LOG.info("filters : {}", filters);

        for (Filter filter : filters) {
            if (head.next == null) {
                LOG.info("set head next");
                head.next = filter;
                continue;
            }
            head = new FilterInvoke(filter, head);
            LOG.info("head is {}", head);
        }

        LOG.info("head is {}", head);
        LOG.info("head.next is {}", head.next);

        return head.invoke(request);
    }

}
