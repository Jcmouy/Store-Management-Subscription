package com.coffee.shop.security.service.impl;

import com.coffee.shop.constants.Constants;
import com.coffee.shop.security.entity.User;
import com.coffee.shop.security.entity.UserExternal;
import com.coffee.shop.security.repository.UserExternalRepository;
import com.coffee.shop.security.repository.UserRepository;
import com.coffee.shop.security.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserExternalRepository userExternalRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(!username.contains(Constants.EXTERNAL_USER_USERNAME)) {
            return getUserInformation(username);
        } else {
            return getUserExternalInformation(username);
        }
    }

    private UserDetails getUserInformation(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return UserDetailsImpl.build(user);
    }

    private UserDetails getUserExternalInformation(String username) {
        UserExternal userExternal = userExternalRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Unable to find external user"));
        return UserExternalDetailsImpl.build(userExternal);
    }
}
