package org.miaohong.fishrpc.core.metrics.source;

import com.codahale.metrics.MetricRegistry;

public interface Source {

    String getName();

    MetricRegistry register();
}
