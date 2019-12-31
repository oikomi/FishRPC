package org.miaohong.fishrpc.core.rpc.chain.filter;

import lombok.ToString;
import org.miaohong.fishrpc.core.annotation.SpiMeta;
import org.miaohong.fishrpc.core.extension.ExtensionLoader;
import org.miaohong.fishrpc.core.rpc.chain.FilterInvoke;
import org.miaohong.fishrpc.core.rpc.chain.FilterOrder;
import org.miaohong.fishrpc.core.rpc.chain.FilterType;
import org.miaohong.fishrpc.core.rpc.client.RPCFuture;
import org.miaohong.fishrpc.core.rpc.client.strategy.ServiceStrategy;
import org.miaohong.fishrpc.core.rpc.client.strategy.StrategyConstants;
import org.miaohong.fishrpc.core.rpc.context.AttributesConstants;
import org.miaohong.fishrpc.core.rpc.context.RpcContext;
import org.miaohong.fishrpc.core.rpc.network.client.transport.NettyClientHandler;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;
import org.miaohong.fishrpc.core.rpc.register.serializer.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpiMeta(name = "message")
@ToString
public class ConsumerMessageFilter extends AbstractFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerMessageFilter.class);

    private ServiceStrategy serviceStrategy = ExtensionLoader.getExtensionLoader(ServiceStrategy.class).
            getExtension(StrategyConstants.STRATEGY_RANDOM);

    public ConsumerMessageFilter() {
        setFilterOrder(FilterOrder.MESSAGE);
        setFilterType(FilterType.CONSUMER);
    }

    @Override
    protected void processBusiness(FilterInvoke invoke, RpcRequest request) {

        LOG.info("send rpc");

        RpcContext context = RpcContext.getContext();
        ServiceInstance serviceInstance = (ServiceInstance) context.getAttributes().get(AttributesConstants.SERVICE_ATTR);

        NettyClientHandler handler = serviceStrategy.getNettyClientHandler(
                serviceInstance.getServerAddr());

        LOG.info("choose handler");

        RPCFuture rpcFuture = handler.sendRequest(request);

        try {
            context.setResponse(rpcFuture.get());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
