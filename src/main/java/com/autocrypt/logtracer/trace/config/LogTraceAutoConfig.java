package com.autocrypt.logtracer.trace.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@AutoConfiguration
@ConditionalOnProperty(value = "log_trace", havingValue = "on")
@Import(LogTraceConfig.class)
public class LogTraceAutoConfig {


    @PostConstruct
    public void logAutoConfigApplied() {
        log.info("LogTraceAutoConfig applied!");
    }
}
