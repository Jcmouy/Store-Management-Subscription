package com.coffee.shop.security.service;

import com.coffee.shop.security.security.jwt.entity.Jwt;

public interface TokenService {

    void setSecretKey(String key, Jwt jwt);

    void deleteToken(String key);

    Object getSecretKey(String key);

}
