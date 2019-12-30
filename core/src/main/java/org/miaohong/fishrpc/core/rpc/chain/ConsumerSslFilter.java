package org.miaohong.fishrpc.core.rpc.chain;

import lombok.ToString;
import org.miaohong.fishrpc.core.annotation.SpiMeta;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpiMeta(name = "ssl")
@ToString
public class ConsumerSslFilter extends AbstractFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerSslFilter.class);

    public ConsumerSslFilter() {
        setFilterOrder(FilterOrder.SSL);
        setFilterType(FilterType.CONSUMER);
    }

    @Override
    public Object invoke(FilterInvoke invoke, RpcRequest request) {
        LOG.info("ConsumerSslFilter start invoke");

        LOG.info("invoke is {}", invoke);

        if (invoke == null) {
            return null;
        }

        return invoke.invoke(request);
    }

}
