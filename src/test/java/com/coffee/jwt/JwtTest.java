package com.coffee.jwt;

import com.coffee.shop.security.security.jwt.model.JwtModel;
import com.coffee.shop.security.security.jwt.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.SignatureException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTest {

    private String USER_USERNAME = "username";

    @Autowired
    JwtUtils jwtUtil;

    @Test
    public void testShouldGenerateAccessToken() throws Exception {
        JwtModel model = this.jwtUtil.generateJwtToken(USER_USERNAME);
        assertNotNull(model);
        assertThat(USER_USERNAME).isEqualTo(model.getSubject());
    }

    @Test
    public void testShouldValidateToken() throws Exception {
        JwtModel model = this.jwtUtil.generateJwtToken(USER_USERNAME);
        assertNotNull(model.getSecretKey());
        assertNotNull(model);
        Claims claims = this.jwtUtil.validateToken(model.getSecretKey(), model);
        assertNotNull(claims);
        assertThat(claims.getSubject()).isEqualTo(model.getSubject());
    }

    @Test(expected = SignatureException.class)
    public void testFailValidateTokenWithInvalidSecretKey() throws NoSuchAlgorithmException {
        String incorrectKey = "incorrectKey";
        JwtModel model = this.jwtUtil.generateJwtToken(USER_USERNAME);
        assertNotNull(incorrectKey);
        assertNotNull(model);
        this.jwtUtil.validateToken(incorrectKey, model);
    }

    @Test(expected = IncorrectClaimException.class)
    public void testFailValidateTokenWithInvalidClaims() throws NoSuchAlgorithmException {
        JwtModel model = this.jwtUtil.generateJwtToken(USER_USERNAME);
        model.setSubject("Fake");
        assertNotNull(model.getSecretKey());
        assertNotNull(model);
        this.jwtUtil.validateToken(model.getSecretKey(), model);
    }

}
