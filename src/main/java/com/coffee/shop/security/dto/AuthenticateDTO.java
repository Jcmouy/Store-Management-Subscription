package com.coffee.shop.security.dto;

import com.coffee.shop.security.security.jwt.model.JwtModel;
import lombok.Data;

import java.util.List;

@Data
public class AuthenticateDTO {

    private Long userDetailsId;
    private String userDetailsUsername;
    private JwtModel jwt;
    private List<String> roles;
}
