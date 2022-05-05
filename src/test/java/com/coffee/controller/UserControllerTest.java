package com.coffee.controller;

import com.coffee.shop.dto.response.InvitesDto;
import com.coffee.shop.entity.Subscribe;
import com.coffee.shop.enums.InviteState;
import com.coffee.shop.model.request.InviteRequest;
import com.coffee.shop.security.entity.Role;
import com.coffee.shop.security.entity.User;
import com.coffee.shop.security.security.jwt.model.JwtModel;
import com.coffee.shop.security.security.jwt.util.JwtUtils;
import com.coffee.shop.security.service.UserService;
import com.coffee.shop.security.service.impl.TokenServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    private String USER_USERNAME = "username";

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    UserService userService;

    @MockBean
    TokenServiceImpl tokenService;

    @Autowired
    JwtUtils jwtUtil;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER", "WRITE"})
    public void testAcceptInvite() throws Exception {
        InviteRequest inviteRequest = new InviteRequest();
        inviteRequest.setUserName(USER_USERNAME);
        inviteRequest.setSubscriptionId(1L);
        given(this.userService.acceptUserInvite(inviteRequest)).willReturn("userNameAdmin");
        String payload = objectMapper.writeValueAsString(inviteRequest);
        this.mockMvc
                .perform(post("/user/acceptInvite")
                        .contentType(APPLICATION_JSON)
                        .content(payload)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testRejectInvite() throws Exception {
        InviteRequest inviteRequest = new InviteRequest();
        inviteRequest.setUserName(USER_USERNAME);
        inviteRequest.setSubscriptionId(1L);
        when(this.userService.acceptUserInvite(inviteRequest)).thenReturn("testUserAdmin");
        String payload = objectMapper.writeValueAsString(inviteRequest);
        this.mockMvc
                .perform(post("/user/rejectInvite")
                        .contentType(APPLICATION_JSON)
                        .content(payload)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testFailGetInvitesMissingRole() throws Exception {
        JwtModel model = this.jwtUtil.generateJwtToken(USER_USERNAME);
        List<InvitesDto> invites = new ArrayList<>();
        InvitesDto invite = new InvitesDto();
        invite.setInviteState("TestInviteState");
        invite.setUserInvitation("TestUserInvitation");
        invite.setSubscriptionId("TestSubscriptionId");
        invites.add(invite);
        when(this.tokenService.getSecretKey(model.getToken())).thenReturn(model);
        when(this.userService.getUserInvites(USER_USERNAME)).thenReturn(invites);
        this.mockMvc
                .perform(get("/user/getInvites/"+USER_USERNAME)
                        .header("Authorization", "Bearer" + model.getToken())
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
