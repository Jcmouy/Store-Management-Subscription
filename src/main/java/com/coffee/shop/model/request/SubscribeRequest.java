package com.coffee.shop.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SubscribeRequest {

    @NotNull(message = "{exception.null}")
    private String userAdmin;

    @NotNull(message = "{exception.null}")
    private String userSubscribe;

}
