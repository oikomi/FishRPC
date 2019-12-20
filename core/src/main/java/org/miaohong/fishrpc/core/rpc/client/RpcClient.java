package org.miaohong.fishrpc.core.rpc.client;


import org.miaohong.fishrpc.core.runtime.RuntimeContext;
import org.miaohong.fishrpc.core.util.ThreadPoolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

public class RpcClient<T> implements Client {

    private static final Logger LOG = LoggerFactory.getLogger(RpcClient.class);

    private static ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtils.newCachedThreadPool(
            4, 16, ThreadPoolUtils.buildQueue(64));

    private ConsumerBootstrap<T> consumerBootstrap;

    public RpcClient(ConsumerConfig<T> consumerConfig) {
        RuntimeContext.now();
        this.consumerBootstrap = new ConsumerBootstrap<>(consumerConfig);
        RuntimeContext.cacheRpcClient(this);
    }

    public T refer() {
        LOG.info("client do refer");
        return consumerBootstrap.refer();
    }

    private void shutDown() {
        if (threadPoolExecutor != null) {
            threadPoolExecutor.shutdown();
        }
        if (consumerBootstrap != null) {
            consumerBootstrap.destroy();
        }
    }

    @Override
    public void destroy() {
        shutDown();
    }

    @Override
    public void destroy(DestroyHook hook) {
        if (hook != null) {
            hook.preDestroy();
        }
        destroy();
        if (hook != null) {
            hook.postDestroy();
        }

    }
}

