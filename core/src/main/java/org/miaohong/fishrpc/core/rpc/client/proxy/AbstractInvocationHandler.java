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

    protected Object checkMethod(Object proxy, Method method, Object[] args) {
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
}
