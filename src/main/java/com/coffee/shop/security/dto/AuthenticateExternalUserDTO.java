package com.coffee.shop.security.dto;

import lombok.Data;

@Data
public class AuthenticateExternalUserDTO extends AuthenticateDTO {

    private String expiration;

}
