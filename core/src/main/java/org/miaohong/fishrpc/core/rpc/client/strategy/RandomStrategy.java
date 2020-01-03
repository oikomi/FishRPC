package org.miaohong.fishrpc.core.rpc.client.strategy;


import org.miaohong.fishrpc.core.annotation.SpiMeta;
import org.miaohong.fishrpc.core.rpc.network.client.transport.NettyClientHandler;
import org.miaohong.fishrpc.core.rpc.register.serializer.ServiceInstance;
import org.miaohong.fishrpc.core.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;


@SpiMeta(name = StrategyConstants.STRATEGY_RANDOM)
public class RandomStrategy extends AbstractServiceStrategy implements ServiceStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(RandomStrategy.class);

    private final Random random = new Random();

    public RandomStrategy() {
        super();
    }

    @Override
    public ServiceInstance getInstance(int timeout) {
        List<ServiceInstance> instances = instanceProvider.getInstances(timeout);
        if (CommonUtils.isEmpty(instances)) {
            return null;
        }
        int thisIndex = random.nextInt(instances.size());
        return instances.get(thisIndex);
    }


    @Override
    public ServiceInstance getInstance() {
        List<ServiceInstance> instances = instanceProvider.getInstances();
        if (CommonUtils.isEmpty(instances)) {
            return null;
        }
        int thisIndex = random.nextInt(instances.size());
        return instances.get(thisIndex);
    }

    @Override
    public NettyClientHandler getNettyClientHandler(String serverAddr) {
        LOG.info("NETTY_CLIENT_HANDLER_CONCURRENT_MAP : {}", NETTY_CLIENT_HANDLER_CONCURRENT_MAP);
        return NETTY_CLIENT_HANDLER_CONCURRENT_MAP.get(serverAddr);
    }

}
