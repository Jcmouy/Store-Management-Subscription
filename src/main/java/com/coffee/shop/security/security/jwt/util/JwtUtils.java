package com.coffee.shop.security.security.jwt.util;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import com.coffee.shop.security.security.jwt.model.JwtModel;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.security.core.Authentication;

@Slf4j
@Component
public class JwtUtils {

    @Value("${coffee.app.jwtSecret}")
    private String jwtSecret;

    @Value("${coffee.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${spring.application.name}")
    private String appName;

    public JwtModel generateJwtToken(String username) {
        if (username == null) {
            throw new NullPointerException("Could not generate jwt token, username is null");
        }
        Date current = new Date();
        Date expiration = new Date((new Date()).getTime() + jwtExpirationMs);

        String token = Jwts.builder()
                .setIssuer(appName)
                .setSubject(username)
                .setIssuedAt(current)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        return JwtModel.createNewJwt(token, appName, username,
                jwtSecret, current, expiration);
    }

    public Claims validateToken(String key, JwtModel model) {
        Claims claims = Jwts.parser()
                .requireIssuer(model.getIssuer())
                .requireSubject(model.getSubject())
                .requireIssuedAt(model.getIssueDate())
                .requireExpiration(model.getExpDate())
                .setSigningKey(key)
                .parseClaimsJws(model.getToken())
                .getBody();
        return claims;
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }
}
