package com.coffee.shop.security.security.jwt.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "jwts")
public class Jwt {

    @Id
    private String token;

    @NotBlank
    @Size(max = 50)
    private String issuer;

    @NotBlank
    @Size(max = 50)
    private String subject;

    @NotBlank
    @Size(max = 120)
    private String secretKey;

    @Temporal(TemporalType.DATE)
    private Date issueDate;

    @Temporal(TemporalType.DATE)
    private Date expDate;

    public Jwt() {
    }

    public Jwt(String token, String issuer, String subject, String secretKey, Date issueDate, Date expDate) {
        this.token = token;
        this.issuer = issuer;
        this.subject = subject;
        this.secretKey = secretKey;
        this.issueDate = issueDate;
        this.expDate = expDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }
}
