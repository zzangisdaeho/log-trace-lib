package com.autocrypt.logtracer.trace.logtrace;

import com.autocrypt.logtracer.trace.TraceId;
import com.autocrypt.logtracer.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalLogTrace implements LogTrace, HttpTrace {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    public static ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

    @Override
    public TraceStatus begin(String message) {
        boolean isNewTrace = syncTraceId();
        TraceId traceId = traceIdHolder.get();

        // HTTP 요청이 첫 번째 레벨인 경우에만 HTTP 요청 정보 로깅
        if (isNewTrace) {
            getHttpRequestLog().ifPresent(httpRequestLog ->
                    log.info("[{}] HTTP REQUEST: {}", traceId.getId(), httpRequestLog)
            );
        }

        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

        return new TraceStatus(traceId, startTimeMs, message);
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();
        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
        } else {
            log.error("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
        }

        releaseTraceId();
    }

    private boolean syncTraceId() {
        TraceId traceId = traceIdHolder.get();
        boolean isNewTrace = false;
        if (traceId == null) {
            traceIdHolder.set(new TraceId());
            isNewTrace = true;
        } else {
            traceIdHolder.set(traceId.createNextId());
        }

        return isNewTrace;
    }

    private void releaseTraceId() {
        TraceId traceId = traceIdHolder.get();
        if (traceId.isFirstLevel()) {
            traceIdHolder.remove();//destroy
        } else {
            traceIdHolder.set(traceId.createPreviousId());
        }
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= level; i++) { // 0부터 시작하여 level 값을 포함하도록 수정
            sb.append( (i == level) ? "|" + prefix : "|   "); // 현재 level에서 prefix 추가
        }
        return sb.toString();
    }
}
