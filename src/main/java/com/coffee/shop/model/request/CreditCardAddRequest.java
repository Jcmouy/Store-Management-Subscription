package com.coffee.shop.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreditCardAddRequest {

    @NotNull(message = "{exception.null}")
    private String userName;

    @NotNull(message = "{exception.null}")
    private String ccType;

    @NotNull(message = "{exception.null}")
    private String ccNumber;

    @NotNull(message = "{exception.null}")
    private String ccExpMonth;

    @NotNull(message = "{exception.null}")
    private String ccExpYear;
}
