package com.coffee.shop.controller;

import com.coffee.shop.dto.response.ResponseDto;
import com.coffee.shop.model.request.SubscribeRequest;
import com.coffee.shop.service.SubscribeService;
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
@RequestMapping(path = "/subscription/subscribe", produces = "application/json")
@CrossOrigin(origins = "*")
public class SubscribeController {

    private final SubscribeService subscribeService;

    @Autowired
    public SubscribeController(SubscribeService subscribeService){
        this.subscribeService = subscribeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user")
    public ResponseEntity<ResponseDto> addUser(@Valid @RequestBody SubscribeRequest subscribeRequest) {
        log.info("SubscribeController:  addUser: {}", subscribeRequest);
        String userName = subscribeService.subscribeUserToSubscription(subscribeRequest);
        return new ResponseEntity<ResponseDto>(new ResponseDto
                ("OK", 201, "User, username " + userName + " successfully subscribed to subscription"), HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/removeUser/{username}")
    public ResponseEntity<ResponseDto> removeUser(@PathVariable("username") String username) {
        log.info("SubscribeController:  removeUser: {}", username);
        String removedUserSubscription = subscribeService.removeUserSubscribe(username);
        if (StringUtils.isEmpty(removedUserSubscription)) {
            return new ResponseEntity<ResponseDto>(new ResponseDto
                    ("FAILED", 404, "User " + username + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<ResponseDto>(new ResponseDto
                ("OK", 204, "User, username " + removedUserSubscription + " successfully removed from subscription"), HttpStatus.OK);
    }
}
