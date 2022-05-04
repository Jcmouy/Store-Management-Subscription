package com.coffee.shop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.coffee.shop.enums.PaymentState;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @NotBlank
    private String paymentInvoice;

    @ManyToOne()
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @NumberFormat
    private Double amountTotal;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentState paymentState;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate created;

    public Payment() {
    }

    public Payment(String paymentInvoice, Subscription subscription, Double amountTotal, PaymentState paymentState, LocalDate created) {
        this.paymentInvoice = paymentInvoice;
        this.subscription = subscription;
        this.amountTotal = amountTotal;
        this.paymentState = paymentState;
        this.created = created;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getPaymentInvoice() {
        return paymentInvoice;
    }

    public void setPaymentInvoice(String paymentInvoice) {
        this.paymentInvoice = paymentInvoice;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Double getAmountTotal() {
        return amountTotal;
    }

    public void setAmountTotal(Double amountTotal) {
        this.amountTotal = amountTotal;
    }

    public PaymentState getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(PaymentState paymentState) {
        this.paymentState = paymentState;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }
}
