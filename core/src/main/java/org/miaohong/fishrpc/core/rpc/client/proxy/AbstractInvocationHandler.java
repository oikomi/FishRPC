package org.miaohong.fishrpc.core.rpc.client.proxy;

import org.miaohong.fishrpc.core.rpc.chain.FilterChain;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;

import java.lang.reflect.Method;
import java.util.UUID;

public abstract class AbstractInvocationHandler {

    private FilterChain filterChain;

    protected boolean isLocalMethod(Method method) {
        return method.getDeclaringClass().equals(Object.class);
    }

    protected void sendMsg(RpcRequest request) {
        filterChain.invoke(request);
    }

    protected RpcRequest buildRequest(Method method, Object[] args) {
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setInterfaceId(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        return request;
    }

}
