package com.coffee.shop.controller;

import com.coffee.shop.dto.response.InvitesDto;
import com.coffee.shop.dto.response.ResponseDto;
import com.coffee.shop.model.request.InviteRequest;
import com.coffee.shop.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/user", produces = "application/json")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getInvites/{username}")
    public ResponseEntity<ResponseDto> getInvites(@PathVariable("username") String username) {
        log.info("UserController:  getInvites user: {}", username);
        List<InvitesDto> invitesDto = userService.getUserInvites(username);
        if (invitesDto.isEmpty()) {
            return new ResponseEntity<ResponseDto>(new ResponseDto
                    ("FAILED", 404, "Unable to find any invites for user, username " + username), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<ResponseDto>(new ResponseDto
                ("OK", 204, "Invites for user, username " + username + " found: " + invitesDto), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/acceptInvite")
    public ResponseEntity<ResponseDto> acceptInvite(@Valid @RequestBody InviteRequest inviteRequest) {
        log.info("UserController:  acceptInvite: {}", inviteRequest);
        String userName = userService.acceptUserInvite(inviteRequest);
        return new ResponseEntity<ResponseDto>(new ResponseDto
                ("OK", 204, "User, username " + userName + " accepted invite to subscription"), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/rejectInvite")
    public ResponseEntity<ResponseDto> rejectInvite(@Valid @RequestBody InviteRequest inviteRequest) {
        log.info("UserController:  rejectInvite: {}", inviteRequest);
        String userName = userService.rejectUserInvite(inviteRequest);
        return new ResponseEntity<ResponseDto>(new ResponseDto
                ("OK", 204, "User, username " + userName + " rejected invite to subscription"), HttpStatus.OK);
    }
}
