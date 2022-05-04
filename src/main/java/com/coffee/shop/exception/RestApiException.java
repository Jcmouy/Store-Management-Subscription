package com.coffee.shop.exception;

import org.springframework.security.core.AuthenticationException;

public class RestApiException extends AuthenticationException {

    public RestApiException(String s) {
        super(s);
    }

    public RestApiException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
