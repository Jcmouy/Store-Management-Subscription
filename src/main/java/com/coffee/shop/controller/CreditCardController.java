package com.coffee.shop.controller;

import com.coffee.shop.dto.response.CreditCardDto;
import com.coffee.shop.dto.response.CreditCardInfoUserDto;
import com.coffee.shop.dto.response.ResponseDto;
import com.coffee.shop.model.request.CreditCardAddRequest;
import com.coffee.shop.model.request.CreditCardRequest;
import com.coffee.shop.service.CreditCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/user/cc", produces = "application/json")
@CrossOrigin(origins = "*")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @Autowired
    public CreditCardController(CreditCardService creditCardService){
        this.creditCardService = creditCardService;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/get")
    public ResponseEntity<ResponseDto> getCreditCard(@Valid @RequestBody CreditCardRequest creditCardRequest) {
        log.info("CreditCardController:  getCreditCard: {}", creditCardRequest);
        CreditCardDto creditCardDto = creditCardService.getCreditCard(creditCardRequest);
        return new ResponseEntity<ResponseDto>(new ResponseDto
                ("OK", 204, "Successfully recover credit card: " + creditCardDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addCreditCard(@Valid @RequestBody CreditCardAddRequest creditCardRequest) {
        log.info("CreditCardController:  addCreditCard: {}", creditCardRequest);
        CreditCardInfoUserDto creditCardInfoUserDto = creditCardService.addCreditCard(creditCardRequest);
        return new ResponseEntity<ResponseDto>(new ResponseDto
                ("OK", 200, "Successfully added credit card for user "
                        + creditCardInfoUserDto.getUserCreditCard()
                        + ", use the following token to recover its information " + creditCardInfoUserDto.getToken()), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/remove")
    public ResponseEntity<ResponseDto> removeCreditCard(@Valid @RequestBody CreditCardRequest creditCardRequest) {
        log.info("CreditCardController:  removeCreditCard: {}", creditCardRequest);
        creditCardService.removeCreditCard(creditCardRequest);
        return new ResponseEntity<ResponseDto>(new ResponseDto
                ("OK", 204, "Successfully removed credit card"), HttpStatus.OK);
    }
}
