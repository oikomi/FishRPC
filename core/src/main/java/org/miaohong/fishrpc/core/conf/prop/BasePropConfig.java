package org.miaohong.fishrpc.core.conf.prop;


import org.apache.commons.configuration2.Configuration;
import org.miaohong.fishrpc.core.util.PropUtils;

public abstract class BasePropConfig implements PropConfig, java.io.Serializable {

    private static final long serialVersionUID = 5429008745932219543L;

    private Configuration configuration;

    public BasePropConfig() {
        this.configuration = PropUtils.loadProperties(getPropertiesPath());
    }

    @Override
    public String getString(String propName) {
        return PropUtils.getStringValue(propName, configuration);
    }

    @Override
    public int getInt(String propName, int defaultValue) {
        return PropUtils.getIntValue(propName, defaultValue, configuration);
    }

    @Override
    public long getLong(String propName, long defaultValue) {
        return PropUtils.getLongValue(propName, defaultValue, configuration);
    }

    @Override
    public boolean getBoolean(String propName, Boolean defaultValue) {
        return PropUtils.getBooleanValue(propName, defaultValue, configuration);
    }

    protected abstract String getPropertiesPath();

}
