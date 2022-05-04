package com.coffee.shop.entity;

import com.coffee.shop.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "credit_card_info_user")
public class CreditCardInfoUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    private String token;

    public CreditCardInfoUser() {
    }

    public CreditCardInfoUser(Long id, User user, String token) {
        this.id = id;
        this.user = user;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
