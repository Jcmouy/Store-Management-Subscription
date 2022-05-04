package com.coffee.shop.security.handler;

import com.coffee.shop.security.security.jwt.util.JwtUtils;
import com.coffee.shop.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class HttpLogoutHandler implements LogoutSuccessHandler {

    private final TokenService tokenService;

    private final JwtUtils jwtUtil;

    @Autowired
    public HttpLogoutHandler(TokenService tokenService, JwtUtils jwtUtil) {
        this.tokenService = tokenService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        String token = this.jwtUtil.parseJwt(request);
        if (token != null) {
            this.tokenService.deleteToken(token);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().flush();
    }
}
