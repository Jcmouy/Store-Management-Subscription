package com.coffee.shop.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PaymentRenewMonthlyChargeDto {

    private List<String> paymentInvoices;
    private String month;
    private String year;

}
