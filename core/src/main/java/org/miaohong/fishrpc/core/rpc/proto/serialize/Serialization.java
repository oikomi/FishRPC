package org.miaohong.fishrpc.core.rpc.proto.serialize;

import org.miaohong.fishrpc.core.annotation.Scope;
import org.miaohong.fishrpc.core.annotation.Spi;

import java.io.IOException;

@Spi(scope = Scope.SINGLETON)
public interface Serialization {

    <T> byte[] serialize(T obj) throws IOException;

    <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException;

}
