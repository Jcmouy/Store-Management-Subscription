package com.coffee.shop.security.service.impl;

import com.coffee.shop.security.repository.TokenRepository;
import com.coffee.shop.security.security.jwt.entity.Jwt;
import com.coffee.shop.security.security.jwt.entity.Token;
import com.coffee.shop.security.security.jwt.exception.TokenNotFoundException;
import com.coffee.shop.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void setSecretKey(String key, Jwt model) {
        Token token = new Token(key, model, model.getExpDate());
        this.tokenRepository.save(token);
    }

    @Override
    public void deleteToken(String key) {
        Optional<Token> token = this.tokenRepository.findById(key);
        if (token.isPresent()){
            this.tokenRepository.delete(token.get());
            return;
        }
        throw new TokenNotFoundException("Token not found");
    }

    @Override
    public Object getSecretKey(String key) {
        Object obj = this.tokenRepository.getOne(key);
        if (obj == null) {
            throw new TokenNotFoundException("Token not found");
        }
        return obj;
    }
}
