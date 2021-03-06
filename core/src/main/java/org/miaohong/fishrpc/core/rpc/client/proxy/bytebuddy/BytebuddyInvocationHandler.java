package org.miaohong.fishrpc.core.rpc.client.proxy.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.miaohong.fishrpc.core.annotation.Internal;
import org.miaohong.fishrpc.core.rpc.chain.ConsumerFilterChain;
import org.miaohong.fishrpc.core.rpc.chain.FilterChain;
import org.miaohong.fishrpc.core.rpc.client.proxy.AbstractInvocationHandler;
import org.miaohong.fishrpc.core.rpc.context.RpcContext;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;

@Internal
public class BytebuddyInvocationHandler extends AbstractInvocationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(BytebuddyInvocationHandler.class);

    private FilterChain filterChain = new ConsumerFilterChain();

    public BytebuddyInvocationHandler() {
    }

    @RuntimeType
    public Object byteBuddyInvoke(@This Object proxy, @Origin Method method, @AllArguments @RuntimeType Object[] args)
            throws ExecutionException, InterruptedException {

        if (isLocalMethod(method)) {
            if (isLocalMethod(method)) {
                return checkMethod(proxy, method, args);
            }
        }

        RpcRequest request = buildRequest(method, args);
        LOG.debug(method.getDeclaringClass().getName());
        LOG.debug(method.getName());

        RpcContext.init(request);

        return filterChain.invoke(request);
    }
}