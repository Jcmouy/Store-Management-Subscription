package com.coffee.shop.service;

import com.coffee.shop.entity.Subscribe;
import com.coffee.shop.model.request.SubscribeRequest;

import java.util.List;

public interface SubscribeService {

    String subscribeUserToSubscription(SubscribeRequest subscribeRequest);

    String removeUserSubscribe(String username);

    Subscribe getSubscribeByUser(String username);

    List<Subscribe> getAllSubscribeByInviteStateAndUser(String inviteState, String username);

    Subscribe getSubscribeBySubscriptionAndUser(Long subscriptionId, String username);

    Subscribe saveSubscribe(Subscribe subscribe);

}
