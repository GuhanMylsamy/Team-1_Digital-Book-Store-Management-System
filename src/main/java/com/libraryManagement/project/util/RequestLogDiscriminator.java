package com.libraryManagement.project.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.Discriminator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestLogDiscriminator implements Discriminator<ILoggingEvent> {

    private static final String KEY = "requestLogFile";
    private boolean started;

    @Override
    public String getDiscriminatingValue(ILoggingEvent event) {
        // This is the dynamic part of the log file name
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String requestURI = attributes.getRequest().getRequestURI().replace("/", "_");
            String method = attributes.getRequest().getMethod();
            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmssSSS").format(new Date());
            return String.format("%s_%s_%s", timestamp, method, requestURI);
        }
        return "unknown_request"; // Fallback for logs outside of a request context
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public boolean isStarted() {
        return started;
    }
}