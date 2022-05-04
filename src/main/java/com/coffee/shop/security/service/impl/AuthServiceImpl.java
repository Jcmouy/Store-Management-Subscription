package com.coffee.shop.security.service.impl;

import com.coffee.shop.constants.Constants;
import com.coffee.shop.security.dto.AuthenticateUserDTO;
import com.coffee.shop.security.entity.Role;
import com.coffee.shop.security.entity.User;
import com.coffee.shop.security.model.reponse.MessageResponseEnum;
import com.coffee.shop.security.model.request.LoginRequest;
import com.coffee.shop.security.model.request.SignupRequest;
import com.coffee.shop.security.repository.UserExternalRepository;
import com.coffee.shop.security.repository.UserRepository;
import com.coffee.shop.security.security.jwt.entity.Jwt;
import com.coffee.shop.security.security.jwt.model.JwtModel;
import com.coffee.shop.security.security.jwt.util.JwtUtils;
import com.coffee.shop.security.service.AuthService;
import com.coffee.shop.service.DiscountService;
import com.coffee.shop.security.service.RoleService;
import com.coffee.shop.security.service.TokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${coffee.app.keySecretExternal}")
    private String keySecretEx;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserExternalRepository userExternalRepository;
    private final RoleService roleService;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final TokenService tokenService;
    private final ModelMapper modelMapper;
    private final DiscountService discountService;

    @Autowired
    public AuthServiceImpl (AuthenticationManager authenticationManager, UserRepository userRepository,
                            RoleService roleService, PasswordEncoder encoder, JwtUtils jwtUtils,
                            TokenService tokenService, ModelMapper modelMapper, DiscountService discountService,
                            UserExternalRepository userExternalRepository){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.tokenService = tokenService;
        this.modelMapper = modelMapper;
        this.discountService = discountService;
        this.userExternalRepository = userExternalRepository;
    }

    @Override
    public AuthenticateUserDTO authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JwtModel jwtModel = jwtUtils.generateJwtToken(authentication.getName());

        this.tokenService.setSecretKey(jwtModel.getToken(), modelMapper.map(jwtModel, Jwt.class));

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetailsImpl.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return getAuthenticationUserDTO(jwtModel, userDetailsImpl, roles);
    }

    @Override
    public String registerUser(SignupRequest signUpRequest) {
        if (verifyExistsByUsername(signUpRequest)){
            return MessageResponseEnum.USERNAME_TAKEN.toString();
        } else if (verifyExistsByEmail(signUpRequest)){
            return MessageResponseEnum.EMAIL_TAKEN.toString();
        }
        return createNewUserAccount(signUpRequest);
    }

    private boolean verifyExistsByUsername(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return true;
        }
        return false;
    }

    private boolean verifyExistsByEmail(SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return true;
        }
        return false;
    }

    private String createNewUserAccount(SignupRequest signUpRequest) {
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = this.roleService.getRoleUser();
            roles.add(userRole);
        } else {
            roles = getRequestedRole(strRoles, roles);
        }
        user.setRoles(roles);
        userRepository.save(user);
        String discountCode = this.discountService.generateDiscountCode();
        String successfullyRegistered = MessageResponseEnum.USER_REGISTERED_SUCCESSFULLY.toString();
        successfullyRegistered = successfullyRegistered.concat(Constants.MESSAGE_DISCOUNT_CODE.concat(discountCode));
        return successfullyRegistered;
    }

    private Set<Role> getRequestedRole(Set<String> strRoles, Set<Role> roles){
        strRoles.forEach(role -> {
            switch (role) {
                case "ROLE_ADMIN":
                    Role adminRole = this.roleService.getRoleAdmin();
                    roles.add(adminRole);
                    break;
                default:
                    Role userRole = this.roleService.getRoleUser();
                    roles.add(userRole);
            }
        });
        return roles;
    }

    private AuthenticateUserDTO getAuthenticationUserDTO(JwtModel jwt, UserDetailsImpl userDetailsUser, List<String> roles) {
        AuthenticateUserDTO authenticateUserDTO = new AuthenticateUserDTO();
        authenticateUserDTO.setUserDetailsId(userDetailsUser.getId());
        authenticateUserDTO.setUserDetailsUsername(userDetailsUser.getUsername());
        authenticateUserDTO.setRoles(roles);
        authenticateUserDTO.setJwt(jwt);
        authenticateUserDTO.setUserDetailsEmail(userDetailsUser.getEmail());
        return authenticateUserDTO;
    }

}
