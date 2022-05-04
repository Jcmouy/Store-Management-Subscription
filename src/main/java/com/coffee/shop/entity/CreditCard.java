package com.coffee.shop.entity;

import com.coffee.shop.enums.CreditCardType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "credit_cards")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CreditCardType ccType;

    @NotBlank
    @Column(length = 16)
    private String ccNumber;

    @NotBlank
    @Column(length = 2)
    private String ccExpMonth;

    @NotBlank
    @Column(length = 4)
    private String ccExpYear;

    @NotBlank
    private String token;

    public CreditCard() {
    }

    public CreditCard(Long id, CreditCardType ccType, String ccNumber, String ccExpMonth, String ccExpYear, String token) {
        this.id = id;
        this.ccType = ccType;
        this.ccNumber = ccNumber;
        this.ccExpMonth = ccExpMonth;
        this.ccExpYear = ccExpYear;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CreditCardType getCcType() {
        return ccType;
    }

    public void setCcType(CreditCardType ccType) {
        this.ccType = ccType;
    }

    public String getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    public String getCcExpMonth() {
        return ccExpMonth;
    }

    public void setCcExpMonth(String ccExpMonth) {
        this.ccExpMonth = ccExpMonth;
    }

    public String getCcExpYear() {
        return ccExpYear;
    }

    public void setCcExpYear(String ccExpYear) {
        this.ccExpYear = ccExpYear;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
