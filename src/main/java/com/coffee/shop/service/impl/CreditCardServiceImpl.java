package com.coffee.shop.service.impl;

import com.coffee.shop.constants.Constants;
import com.coffee.shop.dto.response.CreditCardDto;
import com.coffee.shop.dto.response.CreditCardInfoUserDto;
import com.coffee.shop.entity.CreditCard;
import com.coffee.shop.entity.CreditCardInfoUser;
import com.coffee.shop.enums.CreditCardType;
import com.coffee.shop.exception.AlreadyExistException;
import com.coffee.shop.exception.NotValidException;
import com.coffee.shop.exception.ObjectNotFoundException;
import com.coffee.shop.model.request.CreditCardAddRequest;
import com.coffee.shop.model.request.CreditCardRequest;
import com.coffee.shop.security.entity.User;
import com.coffee.shop.security.repository.CreditCardRepository;
import com.coffee.shop.service.CreditCardInfoUserService;
import com.coffee.shop.service.CreditCardService;
import com.coffee.shop.security.service.UserService;
import com.coffee.shop.util.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    private final UserService userService;
    private final CreditCardRepository creditCardRepository;
    private final CreditCardInfoUserService creditCardInfoUserService;

    @Autowired
    public CreditCardServiceImpl(CreditCardRepository creditCardRepository, CreditCardInfoUserService creditCardInfoUserService,
                                 UserService userService){
        this.creditCardRepository = creditCardRepository;
        this.creditCardInfoUserService = creditCardInfoUserService;
        this.userService = userService;
    }

    @Override
    public CreditCardDto getCreditCard(CreditCardRequest creditCardRequest) {
        CreditCard creditCard = this.getCreditCardByToken(creditCardRequest.getToken());
        return createCreditCardDto(creditCard);
    }

    private CreditCardDto createCreditCardDto(CreditCard creditCard) {
        CreditCardDto creditCardDto = new CreditCardDto();
        creditCardDto.setCcType(creditCard.getCcType().name());
        creditCardDto.setCcNumber(creditCard.getCcNumber());
        creditCardDto.setCcExpMonth(creditCard.getCcExpMonth());
        creditCardDto.setCcExpYear(creditCard.getCcExpYear());
        return creditCardDto;
    }

    @Transactional
    @Override
    public CreditCardInfoUserDto addCreditCard(CreditCardAddRequest creditCardRequest) {
        if (this.creditCardRepository.findByCcNumber(creditCardRequest.getCcNumber()) != null) {
            throw new AlreadyExistException("The credit card number that is being submitted is" +
                    " already exist in the database!");
        }
        validateCard(creditCardRequest.getCcNumber());
        String token = generateToken();
        return createCreditCard(creditCardRequest, token);
    }

    private void validateCard(String ccNumber) {
        if (!CreditCardType.isValid(Long.valueOf(ccNumber))){
            throw new NotValidException("The credit card that was submitted is not valid!");
        }
    }

    private String generateToken() {
        return Text.getAlphaNumericString(Constants.RANDOM_TOKEN_LENGTH);
    }

    private CreditCardInfoUserDto createCreditCard(CreditCardAddRequest creditCardRequest, String token) {
        this.creditCardRepository.save(generateCreditCard(creditCardRequest,token));
        CreditCardInfoUser creditCardInfoUser = this.creditCardInfoUserService.saveCreditCardInfoUser(generateCreditCardInfoUser(creditCardRequest.getUserName(), token));
        return createCreditCardInfoUserDto(creditCardInfoUser);
    }

    private CreditCard generateCreditCard(CreditCardAddRequest creditCardRequest, String token) {
        CreditCard creditCard = new CreditCard();
        creditCard.setCcNumber(creditCardRequest.getCcNumber());
        creditCard.setCcType(getCreditCardType(creditCardRequest.getCcType()));
        creditCard.setCcExpMonth(creditCardRequest.getCcExpMonth());
        creditCard.setCcExpYear(creditCardRequest.getCcExpYear());
        creditCard.setToken(token);
        return creditCard;
    }

    private CreditCardType getCreditCardType(String ccType) {
        return CreditCardType.getCreditCardType(ccType);
    }

    private CreditCardInfoUser generateCreditCardInfoUser(String username, String token) {
        CreditCardInfoUser creditCardInfoUser = new CreditCardInfoUser();
        creditCardInfoUser.setUser(getUserCreditCard(username));
        creditCardInfoUser.setToken(token);
        return creditCardInfoUser;
    }

    private User getUserCreditCard(String username) {
        return this.userService.getUserByUsername(username);
    }

    private CreditCardInfoUserDto createCreditCardInfoUserDto(CreditCardInfoUser creditCardInfoUser) {
        CreditCardInfoUserDto creditCardInfoUserDto = new CreditCardInfoUserDto();
        creditCardInfoUserDto.setUserCreditCard(creditCardInfoUser.getUser().getUsername());
        creditCardInfoUserDto.setToken(creditCardInfoUser.getToken());
        return creditCardInfoUserDto;
    }

    private CreditCard getCreditCardByToken(String token) {
        CreditCard creditCard = this.creditCardRepository.findByToken(token);
        if (creditCard == null) {
            throw new ObjectNotFoundException("Could not find credit card" +
                    " in the database!");
        }
        return creditCard;
    }

    @Transactional
    @Override
    public void removeCreditCard(CreditCardRequest creditCardRequest) {
        removeCreditCardData(creditCardRequest.getToken());
        removeCreditCardInfoUser(creditCardRequest.getToken());
    }

    private void removeCreditCardData(String token) {
        CreditCard creditCard = this.getCreditCardByToken(token);
        this.creditCardRepository.deleteById(creditCard.getId());
    }

    private void removeCreditCardInfoUser(String token) {
        this.creditCardInfoUserService.removeCreditCardInfoUser(token);
    }
}
