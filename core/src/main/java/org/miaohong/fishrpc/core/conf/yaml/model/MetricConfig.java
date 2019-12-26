package org.miaohong.fishrpc.core.conf.yaml.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class MetricConfig implements ModelConfig {

    @Getter
    @Setter
    private List<String> sources;

    @Getter
    @Setter
    private List<String> sinks;
}
