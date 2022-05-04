package com.coffee.shop.security.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class LoginAuditListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        log.info("Successfully authenticated: " + "user=" + auth.getName() + "; " + "date=" + new Date().getTime());
    }
}
