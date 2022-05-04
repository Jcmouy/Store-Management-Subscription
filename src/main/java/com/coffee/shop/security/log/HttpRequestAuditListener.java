package com.coffee.shop.security.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@Component
@Slf4j
public class HttpRequestAuditListener implements ApplicationListener<ServletRequestHandledEvent> {

    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        log.info("Request details: " + "user=" + event.getUserName() + "; " +
                "url=" + event.getRequestUrl() + "; " +
                "method=" + event.getMethod() + "; " +
                "processing-time=" + event.getProcessingTimeMillis() + "; " +
                "status=" + event.getStatusCode() + "; " +
                "date=" + event.getTimestamp() + ";");
    }
}
