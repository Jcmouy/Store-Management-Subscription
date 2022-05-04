package com.coffee.shop.security.security.jwt.model;

import lombok.Data;
import java.util.Date;

@Data
public class JwtModel {

    private String token;
    private String issuer;
    private String subject;
    private String secretKey;
    private Date issueDate;
    private Date expDate;

    public static JwtModel createNewJwt(String token, String appName, String username, String jwtSecret, Date current, Date expiration) {
        JwtModel jwtModel = new JwtModel();
        jwtModel.setToken(token);
        jwtModel.setIssuer(appName);
        jwtModel.setSubject(username);
        jwtModel.setSecretKey(jwtSecret);
        jwtModel.setIssueDate(current);
        jwtModel.setExpDate(expiration);
        return jwtModel;
    }
}
