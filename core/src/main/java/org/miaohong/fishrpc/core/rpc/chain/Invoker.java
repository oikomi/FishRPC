package org.miaohong.fishrpc.core.rpc.chain;

import org.miaohong.fishrpc.core.rpc.proto.RpcRequest;

public interface Invoker {

    Object invoke(RpcRequest request);

}
