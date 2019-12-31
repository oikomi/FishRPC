package org.miaohong.fishrpc.core.rpc.chain.filter;

import lombok.ToString;
import org.miaohong.fishrpc.core.annotation.SpiMeta;
import org.miaohong.fishrpc.core.execption.ClientCoreException;
import org.miaohong.fishrpc.core.execption.CoreErrorMsg;
import org.miaohong.fishrpc.core.extension.ExtensionLoader;
import org.miaohong.fishrpc.core.rpc.chain.FilterInvoke;
import org.miaohong.fishrpc.core.rpc.chain.FilterOrder;
import org.miaohong.fishrpc.core.rpc.chain.FilterType;
import org.miaohong.fishrpc.core.rpc.client.strategy.ServiceStrategy;
import org.miaohong.fishrpc.core.rpc.client.strategy.StrategyConstants;
import org.miaohong.fishrpc.core.rpc.context.AttributesConstants;
import org.miaohong.fishrpc.core.rpc.context.RpcContext;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;
import org.miaohong.fishrpc.core.rpc.register.serializer.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpiMeta(name = "lb")
@ToString
public class ConsumerLbFilter extends AbstractFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerLbFilter.class);

    private ServiceStrategy serviceStrategy = ExtensionLoader.getExtensionLoader(ServiceStrategy.class).
            getExtension(StrategyConstants.STRATEGY_RANDOM);

    public ConsumerLbFilter() {
        setFilterOrder(FilterOrder.LB);
        setFilterType(FilterType.CONSUMER);
    }

    @Override
    protected void processBusiness(FilterInvoke invoke, RpcRequest request) {

        ServiceInstance serviceInstance = serviceStrategy.getInstance(1000);

        LOG.info("serviceInstance : {}", serviceInstance);

        if (serviceInstance == null) {
            throw new ClientCoreException(new CoreErrorMsg(-1, 1001, "cantnot find service"));
        }

        RpcContext context = RpcContext.getContext();
        context.getAttributes().put(AttributesConstants.SERVICE_ATTR, serviceInstance);

    }

}
