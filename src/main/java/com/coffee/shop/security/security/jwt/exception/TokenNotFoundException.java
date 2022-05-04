package com.coffee.shop.security.security.jwt.exception;

import com.coffee.shop.exception.RestApiException;

public class TokenNotFoundException extends RestApiException {

    public TokenNotFoundException(String s) {
        super(s);
    }

    public TokenNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
