package com.coffee.controller;

import com.coffee.shop.model.request.SubscribeRequest;
import com.coffee.shop.service.SubscribeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscribeControllerTest {

    private String USER_ADMIN = "userAdmin";
    private String USER_SUBSCRIBE = "userSubscribe";

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    SubscribeService subscribeService;

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
    @WithMockUser(authorities = {"ROLE_ADMIN", "WRITE"})
    public void testAddUser() throws Exception {
        SubscribeRequest subscribeRequest = new SubscribeRequest();
        subscribeRequest.setUserAdmin(USER_ADMIN);
        subscribeRequest.setUserSubscribe(USER_SUBSCRIBE);
        when(this.subscribeService.subscribeUserToSubscription(subscribeRequest)).thenReturn(USER_SUBSCRIBE);
        String payload = objectMapper.writeValueAsString(subscribeRequest);
        this.mockMvc
                .perform(post("/subscription/subscribe/user")
                        .contentType(APPLICATION_JSON)
                        .content(payload)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN", "WRITE"})
    public void testRemoveUser() throws Exception {
        given(this.subscribeService.removeUserSubscribe(USER_SUBSCRIBE)).willReturn("USER_SUBSCRIBE");
        this.mockMvc
                .perform(delete("/subscription/subscribe/removeUser/"+USER_SUBSCRIBE))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
