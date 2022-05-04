package com.coffee.shop.security.security.jwt.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    private String key;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "jwt_token", referencedColumnName = "token")
    private Jwt jwt;

    @Temporal(TemporalType.DATE)
    private Date expiration;

    public Token() {
    }

    public Token(String key, Jwt jwt, Date expiration) {
        this.key = key;
        this.jwt = jwt;
        this.expiration = expiration;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
}
