package org.miaohong.fishrpc.core.rpc.client.proxy;

import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;
import org.miaohong.fishrpc.core.util.SnowFlake;
import org.miaohong.fishrpc.core.util.SnowFlakeUniq;

import java.lang.reflect.Method;

public abstract class AbstractInvocationHandler {

    private SnowFlake snowFlake = new SnowFlake(SnowFlakeUniq.REQ_ID);

    protected boolean isLocalMethod(Method method) {
        return method.getDeclaringClass().equals(Object.class);
    }

    protected RpcRequest buildRequest(Method method, Object[] args) {
        RpcRequest request = new RpcRequest();
        request.setRequestId(String.valueOf(snowFlake.nextId()));
        request.setInterfaceId(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);

        return request;
    }

}
