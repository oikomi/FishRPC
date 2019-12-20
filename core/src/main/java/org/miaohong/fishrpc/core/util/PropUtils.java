package org.miaohong.fishrpc.core.util;


import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public final class PropUtils {

    private static final Logger LOG = LoggerFactory.getLogger(PropUtils.class);

    private PropUtils() {
        throw new AssertionError();
    }

    public static Properties loadProperties(String path) {
        LOG.info("load properties from file: {}", path);
        Properties properties = new Properties();
        try {
            InputStream in = PropUtils.class.getClassLoader().getResourceAsStream(path);
            properties.load(in);
        } catch (Exception ex) {
            LOG.error("can not read properties from file: {}, msg: {}", path, ex.getMessage());
        }
        return properties;
    }


    public static InputStream loadUrl(String path) {
        return PropUtils.class.getClassLoader().getResourceAsStream(path);
    }

    public static boolean propertiesExist(String path) {
        URL url = PropUtils.class.getClassLoader().getResource(path);
        return null != url;
    }

    public static int getIntValue(String key, int defaultValue, Properties props) {
        if (!props.containsKey(key)) {
            LOG.info("not setting {} value, used default value: {}", key, defaultValue);
            return defaultValue;
        }

        String valueString = (String) props.get(key);
        int value = defaultValue;
        if (!Strings.isNullOrEmpty(valueString)) {
            try {
                value = Integer.parseInt(valueString);
            } catch (Exception ex) {
                LOG.error("failed to convert {} value: {}, used default value: {}",
                        key, valueString, defaultValue, ex);
            }
        }
        return value;
    }

    public static String getStringValue(String key, Properties props) {
        return (String) props.get(key);
    }

    public static long getLongValue(String key, long defaultValue, Properties props) {
        if (!props.containsKey(key)) {
            LOG.info("not setting {} value, used default value: {}", key, defaultValue);
            return defaultValue;
        }

        String valueString = (String) props.get(key);
        long value = defaultValue;
        if (!Strings.isNullOrEmpty(valueString)) {
            try {
                value = Long.parseLong(valueString);
            } catch (Exception ex) {
                LOG.error("failed to convert {} value: {}, used default value: {}",
                        key, valueString, defaultValue, ex);
            }
        }
        return value;
    }

    public static boolean getBooleanValue(String key, Boolean defaultValue, Properties props) {
        if (!props.containsKey(key)) {
            LOG.info("not setting {} value, used default value: {}", key, defaultValue);
            return defaultValue;
        }

        String valueString = (String) props.get(key);
        Boolean value = defaultValue;
        if (!Strings.isNullOrEmpty(valueString)) {
            try {
                value = Boolean.parseBoolean(valueString);
            } catch (Exception ex) {
                LOG.error("failed to convert {} value: {}, used default value: {}",
                        key, valueString, defaultValue, ex);
            }
        }
        return value;
    }


}
