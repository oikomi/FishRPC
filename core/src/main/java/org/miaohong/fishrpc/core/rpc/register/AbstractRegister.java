package org.miaohong.fishrpc.core.rpc.register;

import org.apache.zookeeper.CreateMode;
import org.miaohong.fishrpc.core.rpc.base.Destroyable;
import org.miaohong.fishrpc.core.rpc.service.config.ServiceConfig;

public abstract class AbstractRegister implements Register {

    protected RegisterPropConfig registerPropConfig;
    protected RegisterRole registerRole;

    public AbstractRegister(RegisterPropConfig registerPropConfig) {
        this.registerPropConfig = registerPropConfig;
    }

    protected CreateMode getCreateMode(final ServiceConfig serviceConfig) {
        CreateMode mode;
        switch (serviceConfig.getServiceType()) {
            case DYNAMIC:
                mode = CreateMode.EPHEMERAL;
                break;
            case DYNAMIC_SEQUENTIAL:
                mode = CreateMode.EPHEMERAL_SEQUENTIAL;
                break;
            default:
                mode = CreateMode.PERSISTENT;
                break;
        }

        return mode;
    }


    @Override
    public void destroy(Destroyable.DestroyHook hook) {
        if (hook != null) {
            hook.preDestroy();
        }
        destroy();
        if (hook != null) {
            hook.postDestroy();
        }
    }

}
