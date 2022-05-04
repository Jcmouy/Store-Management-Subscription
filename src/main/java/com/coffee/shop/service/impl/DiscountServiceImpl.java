package com.coffee.shop.service.impl;

import com.coffee.shop.constants.Constants;
import com.coffee.shop.entity.Discount;
import com.coffee.shop.enums.DiscountState;
import com.coffee.shop.security.entity.User;
import com.coffee.shop.security.repository.DiscountRepository;
import com.coffee.shop.service.DiscountService;
import com.coffee.shop.security.service.UserService;
import com.coffee.shop.util.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final UserService userService;

    @Autowired
    public DiscountServiceImpl(DiscountRepository discountRepository, UserService userService) {
        this.discountRepository = discountRepository;
        this.userService = userService;
    }

    @Override
    public String generateDiscountCode() {
        String randomCode = generateRandomStringCode();
        Double randomPercentage = generateRandomPercentage();
        return saveDiscount(randomCode, randomPercentage);
    }

    private String generateRandomStringCode() {
        return Text.getAlphaNumericString(Constants.RANDOM_STRING_LENGTH);
    }

    private Double generateRandomPercentage() {
        Random r = new Random();
        return (double) r.nextInt(30);
    }

    private String saveDiscount(String randomCode, Double randomPercentage) {
        Discount discount = new Discount();
        discount.setDiscountCode(randomCode);
        discount.setAmountDiscount(randomPercentage);
        discount.setDiscountState(DiscountState.ENABLED);
        this.discountRepository.save(discount);
        return randomCode;
    }

    @Override
    public Discount getDiscount(String discountCode) {
        return this.discountRepository.findDiscountByDiscountCode(discountCode);
    }

    @Override
    public void disableDiscountCode(String discountCode, String username) {
        User user = userService.getUserByUsername(username);
        Discount discount = discountRepository.findDiscountByDiscountCode(discountCode);
        discount.setUser(user);
        discount.setDiscountState(DiscountState.DISABLED);
        discountRepository.save(discount);
    }
}
