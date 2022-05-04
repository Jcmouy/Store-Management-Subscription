package com.coffee.shop.security.service;

import com.coffee.shop.security.security.jwt.entity.Jwt;

public interface JwtService {

    Jwt getByToken(String key);

}
