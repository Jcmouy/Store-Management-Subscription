package com.coffee.shop.security.service;

import com.coffee.shop.security.entity.Role;

public interface RoleService {

    Role getRoleUser();

    Role getRoleAdmin();

    Role getRoleExternal();
}
