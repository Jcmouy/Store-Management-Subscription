package com.coffee.shop.security.controller;

import javax.validation.Valid;

import com.coffee.shop.security.dto.AuthenticateUserDTO;
import com.coffee.shop.security.model.reponse.JwtResponse;
import com.coffee.shop.security.model.reponse.MessageResponse;
import com.coffee.shop.security.model.reponse.MessageResponseEnum;
import com.coffee.shop.security.model.request.LoginRequest;
import com.coffee.shop.security.model.request.SignupRequest;
import com.coffee.shop.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController (AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthenticateUserDTO authenticateUserDTO = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(new JwtResponse(authenticateUserDTO.getJwt().getToken(),
                authenticateUserDTO.getUserDetailsId(),
                authenticateUserDTO.getUserDetailsUsername(),
                authenticateUserDTO.getUserDetailsEmail(),
                authenticateUserDTO.getRoles()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        String registerUserResponse = authService.registerUser(signUpRequest);
        if (MessageResponseEnum.USERNAME_TAKEN.toString().equals(registerUserResponse)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        } else if (MessageResponseEnum.EMAIL_TAKEN.toString().equals(registerUserResponse)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        return ResponseEntity.ok(new MessageResponse(registerUserResponse));
    }

}
