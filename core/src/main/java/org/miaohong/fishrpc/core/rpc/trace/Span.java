package org.miaohong.fishrpc.core.rpc.trace;

import com.google.common.base.Preconditions;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

public class Span {

    @Getter
    private final String traceId;

    @Getter
    private final String spanId;

    @Getter
    private final String parentSpanId;

    private final SpanPurpose spanPurpose;

    private final long spanStartTimeEpochMicros;
    private final long spanStartTimeNanos;
    private Long durationNanos;


    public Span(String traceId, String parentSpanId, String spanId, SpanPurpose spanPurpose,
                long spanStartTimeEpochMicros, Long spanStartTimeNanos, Long durationNanos) {

        Preconditions.checkNotNull(traceId, "traceId cannot be null");
        Preconditions.checkNotNull(spanId, "spanId cannot be null");

        this.traceId = traceId;
        this.spanId = spanId;
        this.parentSpanId = parentSpanId;
        this.spanPurpose = spanPurpose;

        this.spanStartTimeEpochMicros = spanStartTimeEpochMicros;
        if (spanStartTimeNanos == null) {
            // No start time nanos was sent. Calculate it as best we can based on spanStartTimeEpochMicros, the current epoch time, and current nano time.
            long currentTimeEpochMicros = TimeUnit.MILLISECONDS.toMicros(System.currentTimeMillis());
            long currentDurationMicros = currentTimeEpochMicros - spanStartTimeEpochMicros;
            long nanoStartTimeOffset = TimeUnit.MICROSECONDS.toNanos(currentDurationMicros);
            spanStartTimeNanos = System.nanoTime() - nanoStartTimeOffset;
        }
        this.spanStartTimeNanos = spanStartTimeNanos;

        this.durationNanos = durationNanos;

    }

    public enum SpanPurpose {
        SERVER,
        CLIENT,
        LOCAL_ONLY,
        UNKNOWN
    }


}
