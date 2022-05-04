package com.coffee.shop.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CredentialRequest {

    @NotNull(message = "{exception.null}")
    private String keyExternal;
}
