package org.miaohong.fishrpc.core.rpc.chain;

import lombok.ToString;
import org.miaohong.fishrpc.core.annotation.SpiMeta;
import org.miaohong.fishrpc.core.execption.ClientCoreException;
import org.miaohong.fishrpc.core.execption.CoreErrorMsg;
import org.miaohong.fishrpc.core.extension.ExtensionLoader;
import org.miaohong.fishrpc.core.rpc.client.RPCFuture;
import org.miaohong.fishrpc.core.rpc.client.strategy.ServiceStrategy;
import org.miaohong.fishrpc.core.rpc.client.strategy.StrategyConstants;
import org.miaohong.fishrpc.core.rpc.network.client.transport.NettyClientHandler;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;
import org.miaohong.fishrpc.core.rpc.register.serializer.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

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
    public Object invoke(FilterInvoke invoke, RpcRequest request) {
        LOG.info("ConsumerMessageFilter start invoke");

        LOG.info("invoke is {}", invoke);

        if (invoke == null) {

            LOG.info("send rpc");

            ServiceInstance serviceInstance = serviceStrategy.getInstance(1000);

            LOG.info("serviceInstance : {}", serviceInstance);

            if (serviceInstance == null) {
                throw new ClientCoreException(new CoreErrorMsg(-1, 1001, "cantnot find service"));
            }

            NettyClientHandler handler = serviceStrategy.getNettyClientHandler(
                    serviceInstance.getServerAddr());

            LOG.info("choose handler");

            RPCFuture rpcFuture = handler.sendRequest(request);

            try {
                return rpcFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
        return invoke.invoke(request);
    }

}
