package com.coffee.shop.service;

import com.coffee.shop.entity.Discount;

public interface DiscountService {

    String generateDiscountCode();

    Discount getDiscount(String discountCode);

    void disableDiscountCode(String discountCode, String username);
}
