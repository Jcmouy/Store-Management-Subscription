package com.coffee.logout;

import com.coffee.shop.security.security.jwt.entity.Jwt;
import com.coffee.shop.security.security.jwt.model.JwtModel;
import com.coffee.shop.security.security.jwt.util.JwtUtils;
import com.coffee.shop.security.service.impl.TokenServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LogoutTest {

    private String USER_USERNAME = "username";

    @MockBean
    TokenServiceImpl tokenService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JwtUtils jwtUtil;

    @Test
    public void testShouldLogoutUser() throws Exception {
        JwtModel model = this.jwtUtil.generateJwtToken(USER_USERNAME);
        this.tokenService.setSecretKey(model.getToken(), modelMapper.map(model, Jwt.class));
        this.mockMvc
                .perform(get("/api/auth/signout")
                        .header("Authorization", "Bearer" + model.getToken())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
