package com.coffee.shop.security.log;

import com.coffee.shop.security.security.jwt.model.JwtModel;
import com.coffee.shop.security.security.jwt.util.JwtUtils;
import com.coffee.shop.security.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@Aspect
@Slf4j
public class LogoutAuditListener {

    private final JwtUtils jwtUtil;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    @Autowired
    public LogoutAuditListener(JwtUtils jwtUtil, JwtService jwtService, ModelMapper modelMapper) {
        this.jwtUtil = jwtUtil;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
    }

    @Before(
            "execution(* org.springframework.security.web.authentication.logout.LogoutSuccessHandler.onLogoutSuccess(..))"
    )
    public void logoutUser(JoinPoint joinPoint) {
        JwtModel model = null;
        try {
            HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
            model = modelMapper.map(this.jwtService.getByToken(this.jwtUtil.parseJwt(request)), JwtModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            String username = model == null ? "no user" : model.getSubject();
            log.info("Logging out: " + "user=" + username + "; " + "date=" + new Date().getTime());
        }
    }

}
