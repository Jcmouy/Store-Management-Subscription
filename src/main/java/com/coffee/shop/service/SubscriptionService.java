package com.coffee.shop.service;

import com.coffee.shop.dto.response.DiscountSubscriptionDto;
import com.coffee.shop.dto.response.ModifySubscriptionDto;
import com.coffee.shop.entity.Subscription;
import com.coffee.shop.model.request.SubscriptionRequest;

public interface SubscriptionService {

    String addSubscription(SubscriptionRequest subscriptionRequest);

    DiscountSubscriptionDto addSubscriptionDiscountCode(SubscriptionRequest subscriptionRequest, String discountCode);

    ModifySubscriptionDto upgradeSubscription(String userName);

    ModifySubscriptionDto downgradeSubscription(String userName);

    String removeSubscription(String userName);

    Subscription saveSubscription(Subscription subscription);

    Subscription getSubscriptionFromUserUsername(String userName);

    Subscription getSubscriptionById(Long id);

}
