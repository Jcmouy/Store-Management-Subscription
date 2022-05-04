package com.coffee.shop.security.service;

import com.coffee.shop.security.dto.AuthenticateUserDTO;
import com.coffee.shop.security.model.request.LoginRequest;
import com.coffee.shop.security.model.request.SignupRequest;

public interface AuthService {

    AuthenticateUserDTO authenticateUser(LoginRequest loginRequest);

    String registerUser(SignupRequest signUpRequest);
}
