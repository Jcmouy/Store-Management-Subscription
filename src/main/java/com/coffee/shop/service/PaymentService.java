package com.coffee.shop.service;

import com.coffee.shop.dto.response.PaymentMonthlyChargeDto;
import com.coffee.shop.dto.response.PaymentRenewMonthlyChargeDto;
import com.coffee.shop.entity.Payment;

public interface PaymentService {

    PaymentMonthlyChargeDto monthlyCharge();

    void savePayment(Payment payment);

    PaymentRenewMonthlyChargeDto renewMonthlyCharge();
}
