package com.coffee.shop.security.dto;

import lombok.Data;

@Data
public class AuthenticateUserDTO extends AuthenticateDTO {

    private String userDetailsEmail;

}
