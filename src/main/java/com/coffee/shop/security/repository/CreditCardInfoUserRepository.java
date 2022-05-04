package com.coffee.shop.security.repository;

import com.coffee.shop.entity.CreditCardInfoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditCardInfoUserRepository extends JpaRepository<CreditCardInfoUser,Long> {

    Optional<CreditCardInfoUser> findByUser_Username(String username);

    void deleteCreditCardInfoUsersByToken(String token);
}
