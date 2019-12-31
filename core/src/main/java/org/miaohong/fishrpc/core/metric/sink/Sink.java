package org.miaohong.fishrpc.core.metric.sink;

public interface Sink {
    void start();

    void stop();

    void report();
}
