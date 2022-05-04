package com.coffee.shop.service;

import com.coffee.shop.dto.response.CreditCardDto;
import com.coffee.shop.dto.response.CreditCardInfoUserDto;
import com.coffee.shop.model.request.CreditCardAddRequest;
import com.coffee.shop.model.request.CreditCardRequest;

public interface CreditCardService {

    CreditCardDto getCreditCard(CreditCardRequest creditCardRequest);

    CreditCardInfoUserDto addCreditCard(CreditCardAddRequest creditCardRequest);

    void removeCreditCard(CreditCardRequest creditCardRequest);
}
