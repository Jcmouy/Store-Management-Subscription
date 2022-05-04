package com.coffee.shop.security.service.impl;

import com.coffee.shop.constants.Constants;
import com.coffee.shop.exception.NotValidException;
import com.coffee.shop.model.request.CredentialRequest;
import com.coffee.shop.security.dto.AuthenticateDTO;
import com.coffee.shop.security.dto.AuthenticateExternalUserDTO;
import com.coffee.shop.security.dto.AuthenticateUserDTO;
import com.coffee.shop.security.entity.Role;
import com.coffee.shop.security.entity.URole;
import com.coffee.shop.security.entity.User;
import com.coffee.shop.security.entity.UserExternal;
import com.coffee.shop.security.model.reponse.MessageResponseEnum;
import com.coffee.shop.security.model.request.LoginRequest;
import com.coffee.shop.security.model.request.SignupRequest;
import com.coffee.shop.security.repository.UserExternalRepository;
import com.coffee.shop.security.repository.UserRepository;
import com.coffee.shop.security.security.jwt.entity.Jwt;
import com.coffee.shop.security.security.jwt.model.JwtModel;
import com.coffee.shop.security.security.jwt.util.JwtUtils;
import com.coffee.shop.security.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthExternalServiceImpl implements AuthExternalService {

    @Value("${coffee.app.keySecretExternal}")
    private String keySecretEx;

    private final AuthenticationManager authenticationManager;
    private final UserExternalRepository userExternalRepository;
    private final RoleService roleService;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final TokenService tokenService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthExternalServiceImpl(AuthenticationManager authenticationManager, RoleService roleService,
                                   PasswordEncoder encoder, JwtUtils jwtUtils, TokenService tokenService,
                                   ModelMapper modelMapper, UserExternalRepository userExternalRepository){
        this.authenticationManager = authenticationManager;
        this.roleService = roleService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
        this.modelMapper = modelMapper;
        this.userExternalRepository = userExternalRepository;
    }

    @Override
    public AuthenticateExternalUserDTO authenticateExternalUser(CredentialRequest credentialRequest) {
        verifyExternalKeyValid(credentialRequest.getKeyExternal());
        return authenticateExternalUser(credentialRequest.getKeyExternal());
    }

    private void verifyExternalKeyValid(String userKeyExternal) {
        if (!userKeyExternal.equals(keySecretEx)){
            throw new NotValidException("The key that was submitted is not valid");
        }
    }

    private AuthenticateExternalUserDTO authenticateExternalUser(String userKeyExternal) {
        String userNameExternal = createExternalUser(userKeyExternal);
        return this.authenticateUser(createExternalLoginRequest(userNameExternal, userKeyExternal));
    }

    private String createExternalUser(String userKeyExternal) {
        Date expirationDate = new Date();
        Long countExternal = this.userExternalRepository.count();
        String userNameExternal = Constants.EXTERNAL_USER_USERNAME.concat(String.valueOf(countExternal));
        UserExternal userExternal = new UserExternal(userNameExternal, encoder.encode(userKeyExternal),
                getDefaultRoleExternal(), new Date(expirationDate.getTime() + (1000 * 60 * 60 * 24)));
        this.userExternalRepository.save(userExternal);
        return userNameExternal;
    }

    public AuthenticateExternalUserDTO authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JwtModel jwtModel = jwtUtils.generateJwtToken(authentication.getName());

        this.tokenService.setSecretKey(jwtModel.getToken(), modelMapper.map(jwtModel, Jwt.class));

        UserExternalDetailsImpl userDetailsExternal = (UserExternalDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetailsExternal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return getAuthenticationExternalUserDTO(jwtModel, userDetailsExternal, roles);
    }

    private LoginRequest createExternalLoginRequest(String userNameExternal, String userKeyExternal) {
        LoginRequest loginExternal = new LoginRequest();
        loginExternal.setUsername(userNameExternal);
        loginExternal.setPassword(userKeyExternal);
        return loginExternal;
    }

    private Set<Role> getDefaultRoleExternal(){
        Set<Role> rolesExternal = new HashSet<>();
        rolesExternal.add(this.roleService.getRoleExternal());
        return rolesExternal;
    }

    private AuthenticateExternalUserDTO getAuthenticationExternalUserDTO(JwtModel jwt, UserExternalDetailsImpl userExternalDetailsUser, List<String> roles) {
        AuthenticateExternalUserDTO authenticateExternalUserDTO = new AuthenticateExternalUserDTO();
        authenticateExternalUserDTO.setUserDetailsId(userExternalDetailsUser.getId());
        authenticateExternalUserDTO.setUserDetailsUsername(userExternalDetailsUser.getUsername());
        authenticateExternalUserDTO.setRoles(roles);
        authenticateExternalUserDTO.setJwt(jwt);
        authenticateExternalUserDTO.setExpiration(userExternalDetailsUser.getExpirationDate());
        return authenticateExternalUserDTO;
    }

}
