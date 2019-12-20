package org.miaohong.fishrpc.core.rpc.server;

import org.miaohong.fishrpc.core.rpc.base.Destroyable;
import org.miaohong.fishrpc.core.rpc.base.LifeCycle;

public abstract class Server implements LifeCycle, Destroyable {

    protected String serverName = "please set server name";

    abstract void setServerName(String serverName);
}
