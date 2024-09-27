package com.autocrypt.logtracer.trace.logtrace;


import com.autocrypt.logtracer.trace.TraceStatus;

public interface LogTrace {

    TraceStatus begin(String message);
    void end(TraceStatus status);
    void exception(TraceStatus status, Exception e);


}
