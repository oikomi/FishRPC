package org.miaohong.fishrpc.core.metric;

import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Preconditions;
import org.miaohong.fishrpc.core.conf.yaml.YamlConfigManager;
import org.miaohong.fishrpc.core.conf.yaml.model.MetricConfig;
import org.miaohong.fishrpc.core.execption.MetricCoreExecption;
import org.miaohong.fishrpc.core.metric.sink.Sink;
import org.miaohong.fishrpc.core.metric.source.Source;
import org.miaohong.fishrpc.core.util.ClassUtils;
import org.miaohong.fishrpc.core.util.CommonUtils;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.miaohong.fishrpc.core.execption.CoreErrorConstant.METRIC_DEFAULT_ERROR;

public class MetricSystem {

    private static final Logger LOG = LoggerFactory.getLogger(MetricSystem.class);

    private static final Objenesis OBJ_GEN = new ObjenesisStd(true);

    private static final String METRIC_CONF_PATH = "config/metric.yaml";

    private MetricConfig metricConfig;

    private volatile boolean started = false;

    private MetricRegistry registry = new MetricRegistry();

    private MetricSystem() {
        this.metricConfig
                = (MetricConfig) YamlConfigManager.get(METRIC_CONF_PATH, MetricConfig.class);
    }

    public static MetricSystem get() {
        return SingletonHolder.INSTANCE;
    }

    private void initCheck() {
        if (started) {
            LOG.info("metric system already started");
            return;
        }
        Preconditions.checkNotNull(metricConfig);
    }

    public void start() {
        LOG.info("start metric system");
        initCheck();
        registerSources();
        registerSinks();
        started = true;
    }

    private void registerSources() {
        List<String> sources = metricConfig.getSources();
        if (!CommonUtils.isEmpty(sources)) {
            sources.forEach(this::registerSource);
        }
    }

    private void registerSource(String clazz) {
        Object o = null;
        try {
            o = ClassUtils.forName(clazz).getConstructor().newInstance();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new MetricCoreExecption(e.getMessage(), METRIC_DEFAULT_ERROR);
        }
        if (o instanceof Source) {
            Source source = (Source) o;
            registry.register(source.getName(), source.register());
        }
    }

    private void registerSinks() {
        List<String> sinks = metricConfig.getSinks();
        if (!CommonUtils.isEmpty(sinks)) {
            sinks.forEach(this::registerSink);
        }
    }

    private void registerSink(String clazz) {
        Object o = null;
        try {
            o = ClassUtils.forName(clazz).getConstructor(MetricRegistry.class).newInstance(registry);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new MetricCoreExecption(e.getMessage(), METRIC_DEFAULT_ERROR);
        }
        if (o instanceof Sink) {
            ((Sink) o).start();
        }
    }

    private static class SingletonHolder {
        private static final MetricSystem INSTANCE = new MetricSystem();
    }

}
