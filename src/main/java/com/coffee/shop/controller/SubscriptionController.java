package com.coffee.shop.controller;

import com.coffee.shop.dto.response.DiscountSubscriptionDto;
import com.coffee.shop.dto.response.ModifySubscriptionDto;
import com.coffee.shop.dto.response.ResponseDto;
import com.coffee.shop.model.request.SubscriptionRequest;
import com.coffee.shop.service.SubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/subscription", produces = "application/json")
@CrossOrigin(origins = "*")
public class SubscriptionController {
	
    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService){
        this.subscriptionService = subscriptionService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addSubscription(@Valid @RequestBody SubscriptionRequest subscriptionRequest) {
        log.info("SubscriptionController:  addSubscription: {}", subscriptionRequest);
        String userName = subscriptionService.addSubscription(subscriptionRequest);
        return new ResponseEntity<ResponseDto>(new ResponseDto
                ("OK", 201, "Subscription is added to user, username " + userName + "."), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add/{discountCode}")
    public ResponseEntity<ResponseDto> addSubscriptionWithDiscountCode(@PathVariable("discountCode") String discountCode,
                                                                       @Valid @RequestBody SubscriptionRequest subscriptionRequest) {
        log.info("SubscriptionController:  addSubscriptionWithDiscountCode: {}", subscriptionRequest);
        DiscountSubscriptionDto discountSubscription = subscriptionService.addSubscriptionDiscountCode(subscriptionRequest, discountCode);
        return new ResponseEntity<ResponseDto>(new ResponseDto
                ("OK", 201, "Subscription is added to user, username "
                        + discountSubscription.getUsername() + " final cost of subscription: " + discountSubscription.getFinalCostSubscription()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/upgrade/{username}")
    public ResponseEntity<ResponseDto> upgradeSubscription(@PathVariable("username") String username) {
        log.info("SubscriptionController:  upgradeSubscription: {}", username);
        ModifySubscriptionDto modifySubscription = subscriptionService.upgradeSubscription(username);
        if (modifySubscription == null) {
            return new ResponseEntity<ResponseDto>(new ResponseDto
                    ("FAILED", 404, "User " + username + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<ResponseDto>(new ResponseDto
                ("OK", 204, "Successfully upgraded subscription of user " + modifySubscription.getUsername() +
                        " to " + modifySubscription.getModifiedPlanName()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/downgrade/{username}")
    public ResponseEntity<ResponseDto> downgradeSubscription(@PathVariable("username") String username) {
        log.info("SubscriptionController:  downgradeSubscription: {}", username);
        ModifySubscriptionDto modifySubscription = subscriptionService.downgradeSubscription(username);
        if (StringUtils.isEmpty(modifySubscription)) {
            return new ResponseEntity<ResponseDto>(new ResponseDto
                    ("FAILED", 404, "User " + username + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<ResponseDto>(new ResponseDto
                ("OK", 204, "Successfully downgraded subscription of user " + modifySubscription.getUsername() +
                        " to " + modifySubscription.getModifiedPlanName()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remove/{username}")
    public ResponseEntity<ResponseDto> removeSubscription(@PathVariable("username") String username) {
        log.info("SubscriptionController:  removeSubscription: {}", username);
        String removedUsername = subscriptionService.removeSubscription(username);
        if (StringUtils.isEmpty(removedUsername)) {
            return new ResponseEntity<ResponseDto>(new ResponseDto
                    ("FAILED", 404, "User " + username + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<ResponseDto>(new ResponseDto
                ("OK", 204, "Subscription of user " + removedUsername + " removed"), HttpStatus.OK);
    }
}
