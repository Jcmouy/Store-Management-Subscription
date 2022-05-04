package com.coffee.shop.security.repository;

import com.coffee.shop.security.entity.URole;
import com.coffee.shop.security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(URole name);
}
