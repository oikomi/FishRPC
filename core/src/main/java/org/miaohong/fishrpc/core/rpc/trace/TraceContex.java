package org.miaohong.fishrpc.core.rpc.trace;

public class TraceContex {

    private static final ThreadLocal<TraceContex> LOCAL_CONTEXT = new ThreadLocal<TraceContex>() {
        @Override
        protected TraceContex initialValue() {
            return new TraceContex();
        }
    };


    public static TraceContex init() {
        TraceContex context = new TraceContex();
        LOCAL_CONTEXT.set(context);
        return context;
    }

    public static void destroy() {
        LOCAL_CONTEXT.remove();
    }

}
