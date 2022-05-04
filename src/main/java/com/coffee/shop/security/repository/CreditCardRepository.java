package com.coffee.shop.security.repository;

import com.coffee.shop.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard,Long> {

    CreditCard findByCcNumber(String cardNumber);

    CreditCard findByToken(String token);
}
