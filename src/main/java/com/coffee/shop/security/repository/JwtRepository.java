package com.coffee.shop.security.repository;

import com.coffee.shop.security.security.jwt.entity.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtRepository extends JpaRepository<Jwt, String> {
}
