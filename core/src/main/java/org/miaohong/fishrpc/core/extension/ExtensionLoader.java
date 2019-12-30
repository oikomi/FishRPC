package org.miaohong.fishrpc.core.extension;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.miaohong.fishrpc.core.annotation.Scope;
import org.miaohong.fishrpc.core.annotation.Spi;
import org.miaohong.fishrpc.core.annotation.SpiMeta;
import org.miaohong.fishrpc.core.execption.FrameworkException;
import org.miaohong.fishrpc.core.util.CommonUtils;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentMap;

public class ExtensionLoader<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ExtensionLoader.class);
    private static final Objenesis OBJ_GEN = new ObjenesisStd(true);
    private static ConcurrentMap<Class<?>, ExtensionLoader<?>> extensionLoaders = Maps.newConcurrentMap();
    private ConcurrentMap<String, T> extensionClasses = Maps.newConcurrentMap();
    private boolean isSingleton = true;
    private Class<T> type;

    private ExtensionLoader(Class<T> type) {
        this.type = type;
        Spi spi = type.getAnnotation(Spi.class);
        if (spi == null) {
            throw new FrameworkException(
                    "Error when load extensible interface " + type + ", must add annotation @Spi.");
        }

        if (spi.scope() == Scope.SINGLETON) {
            this.isSingleton = true;
        } else if (spi.scope() == Scope.PROTOTYPE) {
            this.isSingleton = false;
        }
    }

    private static <T> void checkInterfaceType(Class<T> clz) {
        if (clz == null) {
            LOG.error("Error extension type is null");
            throw new FrameworkException("Error extension type is null");
        }

        if (!clz.isInterface() || !Modifier.isAbstract(clz.getModifiers())) {
            LOG.error("Error extension class must be interface or abstract class, clz is " + clz.getName());
            throw new FrameworkException("Error extension class must be interface or abstract class, clz is " + clz.getName());
        }

        if (!isSpiType(clz)) {
            LOG.error("Error extension type without @SpiMeta annotation, clz is " + clz.getName());
            throw new FrameworkException("Error extension type without @SpiMeta annotation, clz is " + clz.getName());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        checkInterfaceType(type);
        ExtensionLoader<T> loader = (ExtensionLoader<T>) extensionLoaders.get(type);
        if (loader == null) {
            loader = initExtensionLoader(type);
        }
        return loader;
    }

    @SuppressWarnings("unchecked")
    public static synchronized <T> ExtensionLoader<T> initExtensionLoader(Class<T> type) {
        ExtensionLoader<T> loader = (ExtensionLoader<T>) extensionLoaders.computeIfAbsent(
                type, (k) -> {
                    return new ExtensionLoader<>(type);
                });

        extensionLoaders.put(type, loader);
        return loader;
    }

    private static <T> boolean isSpiType(Class<T> clz) {
        return clz.isAnnotationPresent(Spi.class);
    }

    private void checkClassInherit(Class<T> clz) {
        if (!type.isAssignableFrom(clz)) {
            LOG.error("Error is not instanceof " + type.getName());
            throw new FrameworkException("Error is not instanceof " + type.getName());
        }
    }

    private void checkClassPublic(Class<T> clz) {
        if (!Modifier.isPublic(clz.getModifiers())) {
            LOG.error("Error is not a public class, clz is " + clz);
            throw new FrameworkException("Error is not a public class, clz is " + clz);
        }
    }

    private void checkConstructorPublic(Class<T> clz) {
        Constructor<?>[] constructors = clz.getConstructors();

        if (CommonUtils.isEmpty(constructors)) {
            LOG.error("Error has no public no-args constructor, clz is " + clz);
            throw new FrameworkException("Error has no public no-args constructor, clz is " + clz);
        }

        for (Constructor<?> constructor : constructors) {
            if (Modifier.isPublic(constructor.getModifiers()) && constructor.getParameterTypes().length == 0) {
                return;
            }
        }

        LOG.error("Error has no public no-args constructor, clz is " + clz);
        throw new FrameworkException("Error has no public no-args constructor, clz is " + clz);
    }

    @SuppressWarnings("unchecked")
    public T getExtension(String name) {
        if (extensionClasses.containsKey(name)) {
            checkClassInherit((Class<T>) extensionClasses.get(name).getClass());
            checkClassPublic((Class<T>) extensionClasses.get(name).getClass());

            if (!isSingleton) {
                checkConstructorPublic((Class<T>) extensionClasses.get(name).getClass());
                return (T) OBJ_GEN.newInstance(extensionClasses.get(name).getClass());
            } else {
                return extensionClasses.get(name);
            }
        } else {
            ServiceLoader<T> sls = ServiceLoader.load(type);
            for (T sl : sls) {
                String extensionName = sl.getClass().getAnnotation(SpiMeta.class).name();
                extensionClasses.putIfAbsent(extensionName, sl);
            }
        }

        return extensionClasses.get(name);
    }


    public List<T> getAllExtension() {
        ServiceLoader<T> ss = ServiceLoader.load(type);
        for (T s : ss) {
            String extensionName = s.getClass().getAnnotation(SpiMeta.class).name();
            extensionClasses.putIfAbsent(extensionName, s);
        }

        return Lists.newArrayList(extensionClasses.values());
    }

}
