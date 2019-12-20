package org.miaohong.fishrpc.core.extension;

import org.junit.Test;
import org.miaohong.fishrpc.core.rpc.proto.serialize.Serialization;

public class ExtensionLoaderTest {

    @Test
    public void testGetExtension() throws InstantiationException, IllegalAccessException {
        Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class).getExtension(Serialization.class, "protobuf");
        System.out.println(serialization);
    }
}
