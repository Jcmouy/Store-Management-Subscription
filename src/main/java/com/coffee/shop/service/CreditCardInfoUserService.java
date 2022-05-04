package com.coffee.shop.service;

import com.coffee.shop.entity.CreditCardInfoUser;

public interface CreditCardInfoUserService {

    CreditCardInfoUser getCreditCardInfoUserByUsername(String username);

    CreditCardInfoUser saveCreditCardInfoUser(CreditCardInfoUser creditCardInfoUser);

    void removeCreditCardInfoUser(String token);
}
