package org.miaohong.fishrpc.core.metric.source;

import com.codahale.metrics.MetricRegistry;

public interface Source {

    String getName();

    MetricRegistry register();
}
