package com.autocrypt.logtracer.trace.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@Slf4j
@AutoConfiguration
@Import(LogTraceConfig.class)
public class LogTraceAutoConfig {


    @PostConstruct
    public void logAutoConfigApplied() {
        log.info("LogTraceAutoConfig applied!");
    }
}
