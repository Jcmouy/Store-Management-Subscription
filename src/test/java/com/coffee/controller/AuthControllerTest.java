package com.coffee.controller;

import com.coffee.shop.security.entity.URole;
import com.coffee.shop.security.model.request.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testSignUpRequest() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("testUser");
        signupRequest.setEmail("testuser@fibos.com");
        signupRequest.setPassword("12345678");
        Set<String> role = new HashSet<>();
        role.add(URole.ROLE_USER.name());
        signupRequest.setRole(role);
        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(signupRequest);
        this.mockMvc
                .perform(post("/user/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(payload)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
