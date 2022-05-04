package com.coffee.shop.security.service;

import com.coffee.shop.dto.response.InvitesDto;
import com.coffee.shop.model.request.InviteRequest;
import com.coffee.shop.security.entity.User;

import java.util.List;

public interface UserService {

    User getUserByUsername(String userName);

    User changeUserRoleToAdmin(User user);

    User changeUserRoleToUser(User user);

    List<InvitesDto> getUserInvites(String username);

    String acceptUserInvite(InviteRequest inviteRequest);

    String rejectUserInvite(InviteRequest inviteRequest);

}
