package org.miaohong.fishrpc.core.rpc.client.proxy.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.miaohong.fishrpc.core.annotation.Internal;
import org.miaohong.fishrpc.core.rpc.client.proxy.AbstractInvocationHandler;
import org.miaohong.fishrpc.core.execption.ClientCoreException;
import org.miaohong.fishrpc.core.execption.CoreErrorMsg;
import org.miaohong.fishrpc.core.rpc.client.RPCFuture;
import org.miaohong.fishrpc.core.rpc.client.strategy.ServiceStrategy;
import org.miaohong.fishrpc.core.rpc.network.client.transport.NettyClientHandler;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;
import org.miaohong.fishrpc.core.rpc.register.serializer.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

@Internal
public class BytebuddyInvocationHandler extends AbstractInvocationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(BytebuddyInvocationHandler.class);

    private ServiceStrategy serviceStrategy;

    public BytebuddyInvocationHandler(ServiceStrategy serviceStrategy) {
        this.serviceStrategy = serviceStrategy;
    }

    @RuntimeType
    public Object byteBuddyInvoke(@This Object proxy, @Origin Method method, @AllArguments @RuntimeType Object[] args)
            throws ExecutionException, InterruptedException {

        if (isLocalMethod(method)) {
            String name = method.getName();
            if ("equals".equals(name)) {
                return proxy == args[0];
            } else if ("hashCode".equals(name)) {
                return System.identityHashCode(proxy);
            } else if ("toString".equals(name)) {
                return proxy.getClass().getName() + "@" +
                        Integer.toHexString(System.identityHashCode(proxy)) +
                        ", with InvocationHandler " + this;
            } else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }

        RpcRequest request = buildRequest(method, args);
        LOG.debug(method.getDeclaringClass().getName());
        LOG.debug(method.getName());

        LOG.info("start choose handler");

        ServiceInstance serviceInstance = serviceStrategy.getInstance();

        if (serviceInstance == null) {
            throw new ClientCoreException(new CoreErrorMsg(-1, 1001, "cantnot find service"));
        }

        LOG.info("serviceInstance : {}", serviceInstance);
        NettyClientHandler handler = serviceStrategy.getNettyClientHandler(
                serviceInstance.getServerAddr());

        LOG.info("choose handler {}", handler);

        RPCFuture rpcFuture = handler.sendRequest(request);

        return rpcFuture.get();
    }
}