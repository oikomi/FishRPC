package org.miaohong.fishrpc.core.rpc.register.listener;

import org.apache.curator.framework.recipes.cache.ChildData;
import org.miaohong.fishrpc.core.rpc.register.serializer.InstanceSerializer;

public interface ServiceCacheListener {

    void onChange(ChildData data, String path, boolean add, InstanceSerializer serializer);
}
