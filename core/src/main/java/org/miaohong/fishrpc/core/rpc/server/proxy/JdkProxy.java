package org.miaohong.fishrpc.core.rpc.server.proxy;

import org.miaohong.fishrpc.core.execption.ServerCoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

import static org.miaohong.fishrpc.core.execption.CoreErrorConstant.SERVER_PROXY_ERROR;

public class JdkProxy {

    private static final Logger LOG = LoggerFactory.getLogger(JdkProxy.class);

    public static Object invoke(Class<?> serviceClass, String methodName, Class<?>[] parameterTypes,
                                Object serviceBean, Object[] parameters) {
        try {
            Method method = serviceClass.getMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(serviceBean, parameters);
        } catch (Exception e) {
            LOG.error("JdkProxy invoke failed {}", e.getMessage(), e);
            throw new ServerCoreException("JdkProxy invoke failed", SERVER_PROXY_ERROR);
        }
    }
}
