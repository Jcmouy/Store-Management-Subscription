package com.coffee.controller;

import com.coffee.shop.dto.response.CreditCardInfoUserDto;
import com.coffee.shop.model.request.CreditCardAddRequest;
import com.coffee.shop.model.request.CreditCardRequest;
import com.coffee.shop.service.CreditCardService;
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

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditCardControllerTest {

    private String USER_ADMIN = "userAdmin";

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    CreditCardService creditCardService;

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
    public void testAddCreditCard() throws Exception {
        CreditCardAddRequest creditCardAddRequest = new CreditCardAddRequest();
        creditCardAddRequest.setUserName(USER_ADMIN);
        creditCardAddRequest.setCcType("Mastercard");
        creditCardAddRequest.setCcNumber("379354508162306");
        creditCardAddRequest.setCcExpMonth("09");
        creditCardAddRequest.setCcExpYear("2022");
        when(this.creditCardService.addCreditCard(creditCardAddRequest)).thenReturn(createCreditCardInfoUserDto());
        String payload = objectMapper.writeValueAsString(creditCardAddRequest);
        this.mockMvc
                .perform(post("/user/cc/add")
                        .contentType(APPLICATION_JSON)
                        .content(payload)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private CreditCardInfoUserDto createCreditCardInfoUserDto() {
        CreditCardInfoUserDto creditCardInfoUserDto = new CreditCardInfoUserDto();
        creditCardInfoUserDto.setUserCreditCard(USER_ADMIN);
        creditCardInfoUserDto.setToken("V2jFqpPJi5o5ZVYTbIpSmTNW6CiriPZ7ckjUoo0GlxnQQC87ob");
        return creditCardInfoUserDto;
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER", "WRITE"})
    public void testRemoveCreditCard() throws Exception {
        CreditCardRequest creditCardRequest = new CreditCardRequest();
        creditCardRequest.setToken("V2jFqpPJi5o5ZVYTbIpSmTNW6CiriPZ7ckjUoo0GlxnQQC87ob");
        this.creditCardService.removeCreditCard(creditCardRequest);
        String payload = objectMapper.writeValueAsString(creditCardRequest);
        this.mockMvc
                .perform(post("/user/cc/remove")
                        .contentType(APPLICATION_JSON)
                        .content(payload)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
