package com.autocrypt.logtracer.trace.logtrace;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

public interface HttpTrace {

    // HTTP 요청 정보를 문자열로 반환하는 메서드 (Headers와 Body 포함)
    default Optional<String> getHttpRequestLog() {
        HttpServletRequest request = getCurrentHttpRequest();
        if (request == null) {
            return Optional.empty(); // HTTP 요청이 없는 경우 Optional.empty() 반환
        }

        StringBuilder requestLog = new StringBuilder();

        // Request Line (HTTP Method, URL)
        requestLog.append(request.getMethod()).append(" ")
                .append(request.getRequestURL());

        // Request Parameters
        requestLog.append("\nParameters: {");
        request.getParameterMap().forEach((paramName, paramValues) -> {
            requestLog.append(paramName).append("=").append(String.join(",", paramValues)).append(", ");
        });
        if (requestLog.lastIndexOf(", ") > 0) {
            requestLog.setLength(requestLog.length() - 2); // 마지막 쉼표 제거
        }
        requestLog.append("}");

        // Request Headers
        requestLog.append("\nHeaders: {");
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            String headerValue = request.getHeader(headerName);
            requestLog.append("\n    ").append(headerName).append(": ").append(headerValue);
        });
        requestLog.append("\n}");

        // Request Body
        getRequestBody(request).ifPresent(body -> {
            requestLog.append("\nBody: {\n    ")
                    .append(body.replaceAll("\n", "\n    ")) // 들여쓰기 추가
                    .append("\n}");
        });


        return Optional.of(requestLog.toString());
    }

    // 현재 HttpServletRequest를 가져오는 메서드
    default HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    // HTTP Request Body를 Optional<String>로 반환하는 메서드
    default Optional<String> getRequestBody(HttpServletRequest request) {
        // ContentCachingRequestWrapper로 래핑되어 있는 경우에만 본문을 가져옴
        if (request instanceof ContentCachingRequestWrapper) {
            ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    String body = new String(buf, wrapper.getCharacterEncoding());
                    return Optional.of(body);
                } catch (UnsupportedEncodingException e) {
                    // 인코딩 예외가 발생한 경우 Optional.empty() 반환
                    return Optional.of("[Unsupported Encoding]");
                }
            }
        }
        // 래핑되지 않았거나 본문이 없는 경우 Optional.empty() 반환
        return Optional.empty();
    }
}
