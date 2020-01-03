package org.miaohong.fishrpc.core.rpc.client.proxy.jdk;

import org.miaohong.fishrpc.core.annotation.Internal;
import org.miaohong.fishrpc.core.rpc.chain.ConsumerFilterChain;
import org.miaohong.fishrpc.core.rpc.chain.FilterChain;
import org.miaohong.fishrpc.core.rpc.client.proxy.AbstractInvocationHandler;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Internal
public class JDKInvocationHandler<T> extends AbstractInvocationHandler implements InvocationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(JDKInvocationHandler.class);

    private FilterChain filterChain = new ConsumerFilterChain();

    public JDKInvocationHandler() {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (isLocalMethod(method)) {
            return checkMethod(proxy, method, args);
        }

        RpcRequest request = buildRequest(method, args);
        LOG.info("send rpc");

        return filterChain.invoke(request);
    }

}
