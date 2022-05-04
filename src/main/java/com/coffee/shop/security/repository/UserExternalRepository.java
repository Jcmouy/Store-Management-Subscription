package com.coffee.shop.security.repository;

import com.coffee.shop.security.entity.UserExternal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserExternalRepository extends JpaRepository<UserExternal, Long> {

    Optional<UserExternal> findByUsername(String username);
}
