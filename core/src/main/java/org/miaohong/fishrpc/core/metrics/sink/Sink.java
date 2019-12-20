package org.miaohong.fishrpc.core.metrics.sink;

public interface Sink {
    void start();

    void stop();

    void report();
}
