package com.coffee.shop.security.service.impl;

import com.coffee.shop.dto.response.InvitesDto;
import com.coffee.shop.entity.Subscribe;
import com.coffee.shop.entity.Subscription;
import com.coffee.shop.enums.InviteState;
import com.coffee.shop.exception.AlreadyExistException;
import com.coffee.shop.exception.ObjectNotFoundException;
import com.coffee.shop.model.request.InviteRequest;
import com.coffee.shop.security.entity.Role;
import com.coffee.shop.security.entity.URole;
import com.coffee.shop.security.entity.User;
import com.coffee.shop.security.repository.UserRepository;
import com.coffee.shop.security.service.RoleService;
import com.coffee.shop.service.SubscribeService;
import com.coffee.shop.service.SubscriptionService;
import com.coffee.shop.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final SubscribeService subscribeService;
    private final SubscriptionService subscriptionService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService,
                           @Lazy SubscribeService subscribeService, @Lazy SubscriptionService subscriptionService){
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.subscribeService = subscribeService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public User getUserByUsername(String userName) {
        log.info("Getting user by username: {}", userName);
        Optional<User> user = userRepository.findByUsername(userName);
        if (user.isPresent()){
            return user.get();
        }
        throw new ObjectNotFoundException("Could not find " + userName +
                " in the database!");
    }

    @Override
    public User changeUserRoleToAdmin(User user) {
        log.info("About to change User role: {}, to admin", user);
        if(URole.ROLE_ADMIN.equals(user.getRoles().iterator().next().getName())) {
             throw new AlreadyExistException("The User " + user.getUsername() +
                     " already has set an admin role!");
         }
         removePreviousRole(user);
        user.setRoles(setNewRoleToUser(getRoleAdmin()));
         return user;
    }

    private Role getRoleAdmin(){
        return roleService.getRoleAdmin();
    }

    @Override
    public User changeUserRoleToUser(User user) {
        log.info("About to change User role: {}, to user", user);
        if(URole.ROLE_USER.equals(user.getRoles().iterator().next().getName())) {
            throw new AlreadyExistException("The User " + user.getUsername() +
                    " already has set an user role!");
        }
        removePreviousRole(user);
        user.setRoles(setNewRoleToUser(getRoleUser()));
        return user;
    }

    private void removePreviousRole(User user) {
        user.getRoles().clear();
    }

    private Role getRoleUser(){
        return roleService.getRoleUser();
    }

    private Set<Role> setNewRoleToUser(Role role) {
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return roles;
    }

    @Override
    public List<InvitesDto> getUserInvites(String username) {
        List<Subscribe> listOfSubscribe = subscribeService.getAllSubscribeByInviteStateAndUser(InviteState.PENDING.name(), username);
        return getInvites(listOfSubscribe);
    }

    private List<InvitesDto> getInvites(List<Subscribe> listOfSubscribe) {
        List<InvitesDto> invitesDtos = new ArrayList<>();
        for (Subscribe subscribe : listOfSubscribe){
            getInvitesFromSubscribe(subscribe, invitesDtos);
        }
        return invitesDtos;
    }

    private void getInvitesFromSubscribe(Subscribe subscribe, List<InvitesDto> invitesDtos) {
        InvitesDto inviteDto = new InvitesDto();
        inviteDto.setSubscriptionId(String.valueOf(subscribe.getSubscription().getId()));
        inviteDto.setUserInvitation(this.getUserByUsername(subscribe.getSubscription().getUsers().iterator().next().getUsername()).getUsername());
        inviteDto.setInviteState(subscribe.getInviteState().name());
        invitesDtos.add(inviteDto);
    }

    @Override
    public String acceptUserInvite(InviteRequest inviteRequest) {
        Subscribe savedAcceptedInvite = changeStateInvite(inviteRequest, InviteState.ACCEPTED);
        addUserToSubscription(inviteRequest);
        return savedAcceptedInvite.getUser().getUsername();
    }

    private void addUserToSubscription(InviteRequest inviteRequest) {
        Subscription subscription = subscriptionService.getSubscriptionById(inviteRequest.getSubscriptionId());
        User user = this.getUserByUsername(inviteRequest.getUserName());
        subscription.getUsers().add(user);
        this.subscriptionService.saveSubscription(subscription);
    }

    @Override
    public String rejectUserInvite(InviteRequest inviteRequest) {
        Subscribe savedRejectedInvite = changeStateInvite(inviteRequest, InviteState.REJECTED);
        return savedRejectedInvite.getUser().getUsername();
    }

    private Subscribe changeStateInvite(InviteRequest inviteRequest, InviteState inviteState) {
        Subscribe subscribe = subscribeService.getSubscribeBySubscriptionAndUser(inviteRequest.getSubscriptionId(), inviteRequest.getUserName());
        subscribe.setInviteState(inviteState);
        return this.subscribeService.saveSubscribe(subscribe);
    }

}
