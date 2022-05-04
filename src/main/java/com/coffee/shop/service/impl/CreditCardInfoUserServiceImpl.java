package com.coffee.shop.service.impl;

import com.coffee.shop.entity.CreditCardInfoUser;
import com.coffee.shop.exception.ObjectNotFoundException;
import com.coffee.shop.security.repository.CreditCardInfoUserRepository;
import com.coffee.shop.service.CreditCardInfoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreditCardInfoUserServiceImpl implements CreditCardInfoUserService {

    private final CreditCardInfoUserRepository creditCardInfoUserRepository;

    @Autowired
    public CreditCardInfoUserServiceImpl(CreditCardInfoUserRepository creditCardInfoUserRepository){
        this.creditCardInfoUserRepository = creditCardInfoUserRepository;
    }

    @Override
    public CreditCardInfoUser getCreditCardInfoUserByUsername(String username) {
        Optional<CreditCardInfoUser> creditCardInfoUser = this.creditCardInfoUserRepository.findByUser_Username(username);
        if (creditCardInfoUser.isPresent()) {
            return creditCardInfoUser.get();
        }
        throw new ObjectNotFoundException("Could not find credit card info in the database!");
    }

    @Override
    public CreditCardInfoUser saveCreditCardInfoUser(CreditCardInfoUser creditCardInfoUser) {
        return this.creditCardInfoUserRepository.save(creditCardInfoUser);
    }

    @Override
    public void removeCreditCardInfoUser(String token) {
        this.creditCardInfoUserRepository.deleteCreditCardInfoUsersByToken(token);
    }
}
