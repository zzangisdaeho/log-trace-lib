package com.autocrypt.logtracer.trace.aspect;

import com.autocrypt.logtracer.trace.TraceStatus;
import com.autocrypt.logtracer.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class LogTraceAspect {

    private final LogTrace logTrace;

    public LogTraceAspect(LogTrace logTrace) {
        this.logTrace = logTrace;
    }

    //com.autocrypt 패키지 하위에 클래스 이름에 'Controller'가 포함된 클래스의 모든 메서드를 포인트컷으로 정의
    @Pointcut("within(com.autocrypt..*Controller*)")
    public void allController() {}

    //com.autocrypt 패키지 하위에 클래스 이름에 'Service'가 포함된 클래스의 모든 메서드를 포인트컷으로 정의
    @Pointcut("within(com.autocrypt..*Service*)")
    public void allService() {}

    //com.autocrypt 패키지 하위에 클래스 이름에 'Repository'가 포함된 클래스의 모든 메서드를 포인트컷으로 정의
    @Pointcut("within(com.autocrypt..*Repository*)")
    public void allRepository() {}

    //Controller 애너테이션에 해당하는 포인트컷 정의
    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controllerBean() {}

    //RestController 애너테이션에 해당하는 포인트컷 정의
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerBean() {}

    //Service 애너테이션에 해당하는 포인트컷 정의
    @Pointcut("within(@org.springframework.stereotype.Service *)")
    public void serviceBean() {}

    //Repository 애너테이션에 해당하는 포인트컷 정의
    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void repositoryBean() {}

    //여러 포인트컷을 결합하여 하나의 포인트컷으로 정의
    @Pointcut("allController() || allService() || allRepository()")
    public void applicationLayer() {}

    @Pointcut("controllerBean() || restControllerBean() || serviceBean() || repositoryBean()")
    public void applicationBean() {}

    @Around("applicationBean() || applicationLayer()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        TraceStatus status = null;
        try {
            String message = joinPoint.getSignature().toLongString();
            status = logTrace.begin(message);

            //로직 호출
            Object result = joinPoint.proceed();

            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
