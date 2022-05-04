package com.coffee.shop.dto.response;

import lombok.Data;

@Data
public class CreditCardDto {

    private String ccType;

    private String ccNumber;

    private String ccExpMonth;

    private String ccExpYear;
}
