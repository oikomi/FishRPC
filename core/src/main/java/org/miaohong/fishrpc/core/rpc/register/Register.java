package org.miaohong.fishrpc.core.rpc.register;

import org.miaohong.fishrpc.core.annotation.Scope;
import org.miaohong.fishrpc.core.annotation.Spi;
import org.miaohong.fishrpc.core.rpc.base.Destroyable;
import org.miaohong.fishrpc.core.rpc.client.ConsumerConfig;
import org.miaohong.fishrpc.core.rpc.service.config.ServiceConfig;


@Spi(scope = Scope.SINGLETON)
public interface Register extends Destroyable {

    void start(RegisterRole registerRole);

    void register(final ServiceConfig config);

    void unRegister(final ServiceConfig config);

    void subscribe(final ConsumerConfig config);

    void unSubscribe();

}
