package com.autocrypt.logtracer.trace.config;

import com.autocrypt.logtracer.trace.aspect.LogTraceAspect;
import com.autocrypt.logtracer.trace.filter.RequestWrappingFilter;
import com.autocrypt.logtracer.trace.logtrace.LogTrace;
import com.autocrypt.logtracer.trace.logtrace.ThreadLocalLogTrace;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "log_trace", havingValue = "on")
@Slf4j
public class LogTraceConfig {

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }

    @Bean
    public FilterRegistrationBean<RequestWrappingFilter> requestWrappingFilter() {
        FilterRegistrationBean<RequestWrappingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestWrappingFilter());
        registrationBean.addUrlPatterns("/*"); // 모든 URL 패턴에 필터 적용
        registrationBean.setOrder(1); // 필터의 우선순위 설정 (낮을수록 우선순위가 높음)
        return registrationBean;
    }

    @PostConstruct
    public void postConstruct() {
        log.info("LogTrace applied!");
    }
}
