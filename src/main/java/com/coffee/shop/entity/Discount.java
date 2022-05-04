package com.coffee.shop.entity;

import com.coffee.shop.enums.DiscountState;
import com.coffee.shop.security.entity.User;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank
    private String discountCode;

    @NumberFormat
    private Double amountDiscount;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DiscountState discountState;

    public Discount() {
    }

    public Discount(Long id, String discountCode, Double amountDiscount, User user, DiscountState discountState) {
        Id = id;
        this.discountCode = discountCode;
        this.amountDiscount = amountDiscount;
        this.user = user;
        this.discountState = discountState;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public Double getAmountDiscount() {
        return amountDiscount;
    }

    public void setAmountDiscount(Double amountDiscount) {
        this.amountDiscount = amountDiscount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DiscountState getDiscountState() {
        return discountState;
    }

    public void setDiscountState(DiscountState discountState) {
        this.discountState = discountState;
    }
}
