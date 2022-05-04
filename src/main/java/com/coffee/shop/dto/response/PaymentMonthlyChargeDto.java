package com.coffee.shop.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PaymentMonthlyChargeDto {

    private List<String> paymentInvoices;
    private Double totalPayment;
    private Long numberOfPayments;
    private String month;
    private String year;

}
