package org.miaohong.fishrpc.core.rpc.proto;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.miaohong.fishrpc.core.rpc.proto.serialize.Serialization;
import org.miaohong.fishrpc.core.extension.ExtensionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    public static final String NAME = "message decoder";
    private static final Logger LOG = LoggerFactory.getLogger(RpcDecoder.class);
    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass) {
        LOG.info("enter RpcDecoder");
        this.genericClass = genericClass;
    }

    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        LOG.info("do decode");

        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj = ExtensionLoader.getExtensionLoader(Serialization.class).getExtension(Serialization.class,
                "protobuf").deserialize(data, genericClass);

        out.add(obj);
    }

}
