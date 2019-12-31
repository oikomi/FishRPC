package org.miaohong.fishrpc.core.rpc.contex;

import lombok.Getter;
import lombok.Setter;
import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;
import org.miaohong.fishrpc.core.rpc.proto.RpcResponse;

import java.util.HashMap;
import java.util.Map;

public class RpcContex {

    private static final ThreadLocal<RpcContex> LOCAL_CONTEXT = new ThreadLocal<RpcContex>() {
        @Override
        protected RpcContex initialValue() {
            return new RpcContex();
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

    @Getter
    @Setter
    private Object result;

    @Getter
    @Setter
    private String clientRequestId = null;

    public static RpcContex getContext() {
        return LOCAL_CONTEXT.get();
    }

    public static RpcContex init(RpcRequest request) {
        RpcContex context = new RpcContex();
        if (request != null) {
            context.setRequest(request);
//            context.setClientRequestId(request.getAttachments().get(URLParamType.requestIdFromClient.getName()));
        }
        LOCAL_CONTEXT.set(context);

        return context;
    }

    public static RpcContex init() {
        RpcContex context = new RpcContex();
        LOCAL_CONTEXT.set(context);
        return context;
    }

    public static void destroy() {
        LOCAL_CONTEXT.remove();
    }

}
