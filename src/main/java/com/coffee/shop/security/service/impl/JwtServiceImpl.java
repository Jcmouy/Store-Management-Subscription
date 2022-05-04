package com.coffee.shop.security.service.impl;

import com.coffee.shop.security.repository.JwtRepository;
import com.coffee.shop.security.security.jwt.entity.Jwt;
import com.coffee.shop.security.security.jwt.exception.TokenNotFoundException;
import com.coffee.shop.security.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtRepository jwtRepository;

    @Autowired
    public JwtServiceImpl(JwtRepository jwtRepository) {
        this.jwtRepository = jwtRepository;
    }

    @Override
    public Jwt getByToken(String key) {
        Optional<Jwt> jwt = this.jwtRepository.findById(key);
        if (jwt.isPresent()){
            return jwt.get();
        }
        throw new TokenNotFoundException("Jwt not found");
    }
}
