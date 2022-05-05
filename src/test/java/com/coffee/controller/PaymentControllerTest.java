package com.coffee.controller;

import com.coffee.shop.dto.response.PaymentMonthlyChargeDto;
import com.coffee.shop.dto.response.PaymentRenewMonthlyChargeDto;
import com.coffee.shop.entity.Payment;
import com.coffee.shop.entity.Subscription;
import com.coffee.shop.enums.PaymentState;
import com.coffee.shop.security.repository.PaymentRepository;
import com.coffee.shop.service.PaymentService;
import com.coffee.shop.service.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    PaymentService paymentService;

    @MockBean
    SubscriptionService subscriptionService;

    @Mock
    PaymentRepository paymentRepository;

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
    @WithMockUser(authorities = {"ROLE_EXTERNAL", "WRITE"})
    public void testChargeMonthly() throws Exception {
        when(this.paymentService.monthlyCharge()).thenReturn(getPaymentMonthlyChargeDto());
        this.mockMvc
                .perform(post("/api/ex/charge"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private PaymentMonthlyChargeDto getPaymentMonthlyChargeDto() {
        PaymentMonthlyChargeDto paymentMonthlyChargeDto = new PaymentMonthlyChargeDto();
        String invoice  = "F-kjidasdasdilaksdjalsdlasdldks";
        List<String> inovices = new ArrayList<>();
        inovices.add(invoice);
        paymentMonthlyChargeDto.setPaymentInvoices(inovices);
        paymentMonthlyChargeDto.setTotalPayment(10.0);
        paymentMonthlyChargeDto.setNumberOfPayments(1L);
        paymentMonthlyChargeDto.setMonth("MAY");
        paymentMonthlyChargeDto.setYear("2022");
        return paymentMonthlyChargeDto;
    }

    @Test
    @WithMockUser(authorities = {"ROLE_EXTERNAL", "WRITE"})
    public void testRenewMonthly() throws Exception {
        Payment payment = new Payment();
        payment.setPaymentInvoice("kjidasdasdilaksdjalsdlasdldks");
        payment.setPaymentState(PaymentState.PAID);
        payment.setAmountTotal(10.0);
        payment.setId(1L);
        payment.setCreated(LocalDate.now());
        List<Payment> payments = new ArrayList<>();
        payments.add(payment);

        Subscription subscription = new Subscription();
        subscription.setId(1L);

        when(this.paymentRepository.findAll()).thenReturn(payments);
        when(this.subscriptionService.getSubscriptionById(1L)).thenReturn(subscription);
        when(this.paymentService.renewMonthlyCharge()).thenReturn(getPaymentRenewMonthlyChargeDto());
        this.mockMvc
                .perform(post("/api/ex/renew"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private PaymentRenewMonthlyChargeDto getPaymentRenewMonthlyChargeDto() {
        PaymentRenewMonthlyChargeDto paymentRenewMonthlyChargeDto = new PaymentRenewMonthlyChargeDto();
        String invoice  = "F-kjidasdasdilaksdjalsdlasdldks";
        List<String> inovices = new ArrayList<>();
        inovices.add(invoice);
        paymentRenewMonthlyChargeDto.setPaymentInvoices(inovices);
        paymentRenewMonthlyChargeDto.setMonth("MAY");
        paymentRenewMonthlyChargeDto.setYear("2022");
        return paymentRenewMonthlyChargeDto;
    }
}
