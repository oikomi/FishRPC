package org.miaohong.fishrpc.core.extension;

import org.junit.Assert;
import org.junit.Test;
import org.miaohong.fishrpc.core.rpc.proto.serialize.Serialization;

public class ExtensionLoaderTest {

    @Test
    public void testGetExtension() {

        Serialization serialization = ExtensionLoader.getExtensionLoader(Serialization.class).getExtension("protobuf");
        System.out.println(serialization);

        Serialization serialization2 = ExtensionLoader.getExtensionLoader(Serialization.class).getExtension("protobuf");
        System.out.println(serialization2);

        Assert.assertTrue(serialization == serialization2);

    }
}
