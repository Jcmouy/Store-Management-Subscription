package com.coffee.security;

import com.coffee.shop.security.entity.Role;
import com.coffee.shop.security.entity.User;
import com.coffee.shop.security.model.request.LoginRequest;
import com.coffee.shop.security.service.RoleService;
import com.coffee.shop.security.service.impl.TokenServiceImpl;
import com.coffee.shop.security.service.impl.UserDetailsImpl;
import com.coffee.shop.security.service.impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticateTest {

    private String USER_USERNAME = "username";
    private String USER_PASS = "password";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    BCryptPasswordEncoder passwordEncoder;

    @MockBean
    TokenServiceImpl tokenService;

    @Autowired
    RoleService roleService;

    @Test
    public void testShouldLogInUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(USER_USERNAME);
        loginRequest.setPassword(USER_PASS);
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(loginRequest);
        User user = new User(USER_USERNAME, null, this.passwordEncoder.encode(USER_PASS));
        Set<Role> roles = new HashSet<>();
        roles.add(this.roleService.getRoleUser());
        roles.add(this.roleService.getRoleAdmin());
        user.setRoles(roles);
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        given(this.userDetailsService.loadUserByUsername(USER_USERNAME)).willReturn(userDetails);
        this.mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(APPLICATION_JSON)
                        .content(payload)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
