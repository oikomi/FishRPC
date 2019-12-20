package org.miaohong.fishrpc.core.rpc.eventbus;

import org.miaohong.fishrpc.core.rpc.eventbus.event.Event;

public interface EventBus {

    void register(Object subscriber);

    void unregister(Object subscriber);

    void post(Event event);

}
