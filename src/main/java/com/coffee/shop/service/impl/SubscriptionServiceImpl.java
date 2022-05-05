package com.coffee.shop.service.impl;

import com.coffee.shop.constants.Constants;
import com.coffee.shop.dto.response.DiscountSubscriptionDto;
import com.coffee.shop.dto.response.ModifySubscriptionDto;
import com.coffee.shop.entity.Discount;
import com.coffee.shop.entity.Payment;
import com.coffee.shop.entity.Plan;
import com.coffee.shop.entity.Subscription;
import com.coffee.shop.enums.DiscountState;
import com.coffee.shop.enums.PaymentState;
import com.coffee.shop.enums.PlanTypes;
import com.coffee.shop.exception.AlreadyExistException;
import com.coffee.shop.exception.NotValidException;
import com.coffee.shop.exception.ObjectNotFoundException;
import com.coffee.shop.model.request.SubscriptionRequest;
import com.coffee.shop.security.entity.User;
import com.coffee.shop.security.repository.SubscriptionRepository;
import com.coffee.shop.security.service.*;
import com.coffee.shop.service.DiscountService;
import com.coffee.shop.service.PaymentService;
import com.coffee.shop.service.PlanService;
import com.coffee.shop.service.SubscriptionService;
import com.coffee.shop.util.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final PlanService planService;
    private final DiscountService discountService;
    private final PaymentService paymentService;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, UserService userService,
                                   PlanService planService, DiscountService discountService,
                                   PaymentService paymentService){
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
        this.planService = planService;
        this.discountService = discountService;
        this.paymentService = paymentService;
    }

    @Transactional
    @Override
    public String addSubscription(SubscriptionRequest subscriptionRequest) {
        checkIfUserIsAlreadySubscribed(subscriptionRequest);
        User user = this.getUser(subscriptionRequest.getUserName());
        Plan plan = this.getPlan(subscriptionRequest.getPlanName());
        Subscription savedSubscription = this.saveSubscription(createSubscription(userRoleToAdmin(user),plan));
        createPayment(savedSubscription);
        return !savedSubscription.getUsers().isEmpty() ? savedSubscription.getUsers().iterator().next().getUsername() : null;
    }

    @Transactional
    @Override
    public DiscountSubscriptionDto addSubscriptionDiscountCode(SubscriptionRequest subscriptionRequest, String discountCode) {
        checkIfUserIsAlreadySubscribed(subscriptionRequest);
        Discount discount = this.discountService.getDiscount(discountCode);
        if (DiscountState.DISABLED.equals(discount.getDiscountState())){
            throw new NotValidException("The discount that is being submitted has already been used ");
        }
        User user = this.getUser(subscriptionRequest.getUserName());
        Plan plan = this.getPlan(subscriptionRequest.getPlanName());
        plan.setChargePerMonth(plan.getChargePerMonth() - (plan.getChargePerMonth() * (discount.getAmountDiscount() / 100)));
        Subscription savedSubscription = this.saveSubscription(createSubscription(userRoleToAdmin(user),plan));
        disableDiscount(discountCode, user.getUsername());
        return getDiscountSubscription(savedSubscription.getUsers().iterator().next().getUsername(), plan.getChargePerMonth());
    }

    private void checkIfUserIsAlreadySubscribed(SubscriptionRequest subscriptionRequest) {
        if (this.getSubscriptionFromUserUsername(subscriptionRequest.getUserName()) != null){
            throw new AlreadyExistException("The User with username " + subscriptionRequest.getUserName() +
                    " already has a subscription!");
        }
    }

    private Subscription createSubscription(User user, Plan plan){
        Subscription subscription = new Subscription();
        Set<User> users = new HashSet<>();
        users.add(user);
        subscription.setPlan(plan);
        subscription.setUsers(users);
        return subscription;
    }

    private void createPayment(Subscription savedSubscription) {
        Payment payment = new Payment();
        payment.setSubscription(savedSubscription);
        payment.setPaymentInvoice(Constants.PAYMENT_PREFIX.concat(Text.getAlphaNumericString(Constants.RANDOM_PAYMENT_LENGTH)));
        payment.setAmountTotal(savedSubscription.getPlan().getChargePerMonth());
        payment.setPaymentState(PaymentState.PENDING);
        payment.setCreated(LocalDate.now());
        this.paymentService.savePayment(payment);
    }

    private void disableDiscount(String discountCode, String username) {
        this.discountService.disableDiscountCode(discountCode, username);
    }

    private DiscountSubscriptionDto getDiscountSubscription(String username, Double costSubscription){
        DiscountSubscriptionDto discountSubscriptionDto = new DiscountSubscriptionDto();
        discountSubscriptionDto.setUsername(username);
        discountSubscriptionDto.setFinalCostSubscription(costSubscription);
        return discountSubscriptionDto;
    }

    @Override
    public ModifySubscriptionDto upgradeSubscription(String userName) {
        Subscription subscription = this.getSubscriptionFromUserUsername(userName);
        if (PlanTypes.FAMILY.equals(subscription.getPlan().getPlanName())){
            throw new AlreadyExistException("Not possible to upgrade the subscription of user " + userName +
                    " is already subscribed to our highest plan: " + subscription.getPlan().getPlanName().name());
        }
        subscription.setPlan(this.getPlan(PlanTypes.FAMILY.name()));
        Subscription savedSubscription = this.saveSubscription(subscription);
        return getModifySubscription(savedSubscription.getUsers().iterator().next().getUsername(), savedSubscription.getPlan());
    }

    @Override
    public ModifySubscriptionDto downgradeSubscription(String userName) {
        Subscription subscription = this.getSubscriptionFromUserUsername(userName);
        if (PlanTypes.PERSONAL.equals(subscription.getPlan().getPlanName())){
            throw new AlreadyExistException("Not possible to downgrade the subscription of user: " + userName +
                    " ,already in our basic plan: " + subscription.getPlan().getPlanName().name() + " , only possible to upgrade or remove!");
        }
        subscription.setPlan(this.getPlan(PlanTypes.PERSONAL.name()));
        verifyFamilyMembersOfSubscription(subscription);
        Subscription savedSubscription = this.saveSubscription(subscription);
        return getModifySubscription(savedSubscription.getUsers().iterator().next().getUsername(), savedSubscription.getPlan());
    }

    private void verifyFamilyMembersOfSubscription(Subscription subscription) {
        if (subscription.getUsers().size() > 1) {
            Set<User> remainingUser = removeFamilyMembersFromSubscription(subscription);
            subscription.setUsers(remainingUser);
        }
    }

    private Set<User> removeFamilyMembersFromSubscription(Subscription subscription) {
        User adminUser = subscription.getUsers().iterator().next();
        subscription.getUsers().clear();
        Set<User> users = new HashSet<>();
        users.add(adminUser);
        return users;
    }

    private ModifySubscriptionDto getModifySubscription(String userName, Plan plan) {
        ModifySubscriptionDto modifySubscriptionDto = new ModifySubscriptionDto();
        modifySubscriptionDto.setUsername(userName);
        modifySubscriptionDto.setModifiedPlanName(plan.getPlanName().name());
        return modifySubscriptionDto;
    }

    @Override
    public String removeSubscription(String userName) {
        User user = this.getUser(userName);
        userRoleToUser(user);
        Subscription subscription = this.getSubscriptionFromUserUsername(userName);
        subscriptionRepository.deleteById(subscription.getId());
        return userName;
    }

    private Plan getPlan(String planName) {
        return planService.getPlanByPlanName(planName);
    }

    private User getUser(String username) {
        return userService.getUserByUsername(username);
    }

    private User userRoleToAdmin(User user) {
        return userService.changeUserRoleToAdmin(user);
    }

    private User userRoleToUser(User user) {
        return userService.changeUserRoleToUser(user);
    }

    @Override
    public Subscription saveSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getSubscriptionFromUserUsername(String userName) {
        return subscriptionRepository.findSubscriptionByUsers_Username(userName);
    }

    @Override
    public Subscription getSubscriptionById(Long id) {
        Optional<Subscription> subscription = subscriptionRepository.findById(id);
        if (subscription.isPresent()){
            return subscription.get();
        }
        throw new ObjectNotFoundException("Could not find subscription: " + id +
                " in the database!");
    }

}
