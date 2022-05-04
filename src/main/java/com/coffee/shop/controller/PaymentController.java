package com.coffee.shop.controller;

import com.coffee.shop.dto.response.PaymentMonthlyChargeDto;
import com.coffee.shop.dto.response.PaymentRenewMonthlyChargeDto;
import com.coffee.shop.dto.response.ResponseDto;
import com.coffee.shop.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/api/ex", produces = "application/json")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasRole('EXTERNAL')")
    @PostMapping("/charge")
    public ResponseEntity<ResponseDto> chargeMonthly() {
        log.info("PaymentController:  chargeMonthly ");
        PaymentMonthlyChargeDto paymentMonthlyChargeDto = paymentService.monthlyCharge();
        if (paymentMonthlyChargeDto == null) {
            return new ResponseEntity<ResponseDto>(new ResponseDto
                    ("FAILED", 404, "Could not find any remaining subscriptions to charge"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<ResponseDto>(new ResponseDto
                ("OK", 204, "Successfully executed current month charges, invoices: "
                        + paymentMonthlyChargeDto.getPaymentInvoices() + ", total amount: " + paymentMonthlyChargeDto.getTotalPayment()
                        + ", number of payments: " + paymentMonthlyChargeDto.getNumberOfPayments()
                        + ", month: " + paymentMonthlyChargeDto.getMonth()
                        + ", year: " + paymentMonthlyChargeDto.getYear()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EXTERNAL')")
    @PostMapping("/renew")
    public ResponseEntity<ResponseDto> renewMonthly() {
        log.info("PaymentController:  renewMonthly ");
        PaymentRenewMonthlyChargeDto paymentRenewMonthlyChargeDto = paymentService.renewMonthlyCharge();
        if (paymentRenewMonthlyChargeDto == null) {
            return new ResponseEntity<ResponseDto>(new ResponseDto
                    ("FAILED", 404, "Could not find any remaining subscriptions to charge"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<ResponseDto>(new ResponseDto
                ("OK", 204, "Successfully executed current month charges, invoices: "
                        + paymentRenewMonthlyChargeDto.getPaymentInvoices()
                        + ", month: " + paymentRenewMonthlyChargeDto.getMonth()
                        + ", year: " + paymentRenewMonthlyChargeDto.getYear()), HttpStatus.OK);
    }
}
