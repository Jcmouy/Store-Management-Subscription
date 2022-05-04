package com.coffee.shop.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class InviteRequest {

    @NotNull(message = "{exception.null}")
    private String userName;

    @NotNull(message = "{exception.null}")
    private Long subscriptionId;

}
