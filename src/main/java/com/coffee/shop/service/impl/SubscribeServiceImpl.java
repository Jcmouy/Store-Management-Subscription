package com.coffee.shop.service.impl;

import com.coffee.shop.entity.Subscribe;
import com.coffee.shop.entity.Subscription;
import com.coffee.shop.enums.InviteState;
import com.coffee.shop.exception.NotValidException;
import com.coffee.shop.exception.ObjectNotFoundException;
import com.coffee.shop.model.request.SubscribeRequest;
import com.coffee.shop.security.entity.User;
import com.coffee.shop.security.repository.SubscribeRepository;
import com.coffee.shop.service.SubscribeService;
import com.coffee.shop.service.SubscriptionService;
import com.coffee.shop.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscribeServiceImpl implements SubscribeService {

    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final SubscribeRepository subscribeRepository;

    @Autowired
    public SubscribeServiceImpl(@Lazy SubscriptionService subscriptionService, UserService userService,
                                SubscribeRepository subscribeRepository){
        this.subscriptionService = subscriptionService;
        this.userService = userService;
        this.subscribeRepository = subscribeRepository;
    }

    @Override
    public String subscribeUserToSubscription(SubscribeRequest subscribeRequest) {
        Subscription subscription = this.subscriptionService.getSubscriptionFromUserUsername(subscribeRequest.getUserAdmin());
        if (subscription == null){
            throw new ObjectNotFoundException("Not possible to locate subscription for user " + subscribeRequest.getUserAdmin());
        }
        validateSubscribeRequest(subscription, subscribeRequest);
        Subscribe savedSubscribe = this.saveSubscribe(createSubscribe(subscription, subscribeRequest.getUserSubscribe()));
        return savedSubscribe.getUser() != null  ? savedSubscribe.getUser().getUsername() : null;
    }

    private void validateSubscribeRequest(Subscription subscription, SubscribeRequest subscribeRequest){
        validateNumberOfUsersInSubscription(subscription);
        validateUserExist(subscribeRequest.getUserSubscribe());
        validateUserIsNotInAnotherSubscription(subscribeRequest.getUserSubscribe());
    }

    private Subscribe createSubscribe(Subscription subscription, String userSubscribe) {
        Subscribe subscribe = new Subscribe();
        User user = getUserSubscribe(userSubscribe);
        subscribe.setSubscription(subscription);
        subscribe.setUser(user);
        subscribe.setInviteState(InviteState.PENDING);
        return subscribe;
    }

    private void validateNumberOfUsersInSubscription(Subscription subscription) {
        if (subscription.getUsers().size() >= 5) {
            throw new NotValidException("The subscription has reached the maximum number of users");
        }
    }

    private void validateUserExist(String userSubscribe) {
        if (getUserSubscribe(userSubscribe) == null) {
            throw new ObjectNotFoundException("Could not find user " + userSubscribe + ", in the database, can't continue with subscribe to subscription");
        }
    }

    private void validateUserIsNotInAnotherSubscription(String userSubscribe) {
        if (this.subscriptionService.getSubscriptionFromUserUsername(userSubscribe) != null) {
            throw new NotValidException("The user is already subscribed in another subscription");
        }
    }

    @Override
    public String removeUserSubscribe(String username) {
        Subscribe subscribe = getSubscribeByUser(username);
        if (subscribe == null) {
            throw new ObjectNotFoundException("User " + username + " is not subscribed to any subscription");
        }
        removeUserSubscription(username);
        this.subscribeRepository.deleteById(subscribe.getId());
        return username;
    }

    private void removeUserSubscription(String username){
        Subscription subscription = this.subscriptionService.getSubscriptionFromUserUsername(username);
        if (subscription == null) {
            return;
        }
        User user = getUserSubscribe(username);
        subscription.getUsers().remove(user);
        this.subscriptionService.saveSubscription(subscription);
    }

    private User getUserSubscribe(String username) {
        return userService.getUserByUsername(username);
    }

    @Override
    public Subscribe getSubscribeByUser(String username) {
        return this.subscribeRepository.findSubscribeByUser_Username(username);
    }

    @Override
    public List<Subscribe> getAllSubscribeByInviteStateAndUser(String inviteState, String username) {
        return this.subscribeRepository.findAllByInviteStateAndUser_Username(InviteState.valueOf(inviteState), username);
    }

    @Override
    public Subscribe getSubscribeBySubscriptionAndUser(Long subscriptionId, String username) {
        return this.subscribeRepository.findBySubscription_IdAndUser_Username(subscriptionId, username);
    }

    @Override
    public Subscribe saveSubscribe(Subscribe subscribe) {
        return this.subscribeRepository.save(subscribe);
    }
}
