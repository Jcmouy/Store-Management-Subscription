package com.coffee.shop.security.service;

import com.coffee.shop.model.request.CredentialRequest;
import com.coffee.shop.security.dto.AuthenticateDTO;
import com.coffee.shop.security.dto.AuthenticateExternalUserDTO;
import com.coffee.shop.security.model.request.LoginRequest;
import com.coffee.shop.security.model.request.SignupRequest;

public interface AuthExternalService {

    AuthenticateExternalUserDTO authenticateExternalUser(CredentialRequest credentialRequest);
}
