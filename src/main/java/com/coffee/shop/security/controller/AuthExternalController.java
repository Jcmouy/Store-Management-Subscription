package com.coffee.shop.security.controller;

import com.coffee.shop.model.request.CredentialRequest;
import com.coffee.shop.security.dto.AuthenticateExternalUserDTO;
import com.coffee.shop.security.model.reponse.JwtResponse;
import com.coffee.shop.security.service.AuthExternalService;
import com.coffee.shop.security.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/ex", produces = "application/json")
@CrossOrigin(origins = "*")
@Slf4j
public class AuthExternalController {

    private final AuthExternalService authExternalService;

    @Autowired
    public AuthExternalController(AuthExternalService authExternalService){
        this.authExternalService = authExternalService;
    }

    @PostMapping("/credential")
    public  ResponseEntity<?> obtainCredentials(@Valid @RequestBody CredentialRequest credentialRequest) {
        log.info("BalanceController:  obtainCredentials: {}", credentialRequest);
        AuthenticateExternalUserDTO authenticateExternalUserDTO = authExternalService.authenticateExternalUser(credentialRequest);
        return ResponseEntity.ok(new JwtResponse(authenticateExternalUserDTO.getJwt().getToken(), authenticateExternalUserDTO.getRoles()));
    }

}
