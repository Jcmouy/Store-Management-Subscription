package com.coffee.shop.security.service.impl;

import com.coffee.shop.security.entity.Role;
import com.coffee.shop.security.entity.URole;
import com.coffee.shop.security.repository.RoleRepository;
import com.coffee.shop.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl (RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleUser(){
        return roleRepository.findByName(URole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    }

    @Override
    public Role getRoleAdmin(){
        return roleRepository.findByName(URole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    }

    @Override
    public Role getRoleExternal() {
        return roleRepository.findByName(URole.ROLE_EXTERNAL)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    }
}
