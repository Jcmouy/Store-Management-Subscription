package com.coffee.shop.security.service.impl;

import com.coffee.shop.security.entity.UserExternal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserExternalDetailsImpl implements UserDetails{
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String password;
    private String expirationDate;
    private Collection<? extends GrantedAuthority> authorities;

    public UserExternalDetailsImpl(Long id, String username, String password, String expirationDate,
                                   Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.expirationDate = expirationDate;
        this.authorities = authorities;
    }

    public static UserExternalDetailsImpl build(UserExternal userExternal) {
        List<GrantedAuthority> authorities = userExternal.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new UserExternalDetailsImpl(
                userExternal.getId(),
                userExternal.getUsername(),
                userExternal.getPassword(),
                String.valueOf(userExternal.getExpiration()),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getId() {
        return id;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserExternalDetailsImpl user = (UserExternalDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
