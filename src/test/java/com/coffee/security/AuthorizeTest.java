package com.coffee.security;

import com.coffee.shop.dto.response.InvitesDto;
import com.coffee.shop.security.security.jwt.model.JwtModel;
import com.coffee.shop.security.security.jwt.util.JwtUtils;
import com.coffee.shop.security.service.UserService;
import com.coffee.shop.security.service.impl.TokenServiceImpl;
import com.coffee.shop.security.service.impl.UserDetailsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizeTest {

    private String USER_USERNAME = "username";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    TokenServiceImpl tokenService;

    @MockBean
    UserService userService;

    @Autowired
    JwtUtils jwtUtil;

    @Test
    @WithMockUser
    public void testShouldAccessAuthorizeRequest() throws Exception {
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
                .andExpect(status().isOk());
    }
}
