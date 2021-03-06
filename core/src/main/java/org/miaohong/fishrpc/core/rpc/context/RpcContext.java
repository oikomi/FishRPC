package org.miaohong.fishrpc.core.rpc.context;

import lombok.Getter;
import lombok.Setter;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;
import org.miaohong.fishrpc.core.rpc.proto.RpcResponse;

import java.util.HashMap;
import java.util.Map;

public class RpcContext {

    private static final ThreadLocal<RpcContext> LOCAL_CONTEXT = new ThreadLocal<RpcContext>() {
        @Override
        protected RpcContext initialValue() {
            return new RpcContext();
        }
    };

    @Getter
    private Map<Object, Object> attributes = new HashMap<>();

    @Getter
    private Map<String, String> attachments = new HashMap<>();// attachment in rpc context. not same with request's attachments

    @Getter
    @Setter
    private RpcRequest request;

    @Getter
    @Setter
    private RpcResponse response;

    public static RpcContext getContext() {
        return LOCAL_CONTEXT.get();
    }

    public static RpcContext init(RpcRequest request) {
        RpcContext context = new RpcContext();
        if (request != null) {
            context.setRequest(request);
        }
        LOCAL_CONTEXT.set(context);

        return context;
    }

    public static RpcContext init() {
        RpcContext context = new RpcContext();
        LOCAL_CONTEXT.set(context);
        return context;
    }

    public static void destroy() {
        LOCAL_CONTEXT.remove();
    }

}
