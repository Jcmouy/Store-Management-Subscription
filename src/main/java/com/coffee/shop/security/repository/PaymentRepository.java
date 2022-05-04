package com.coffee.shop.security.repository;

import com.coffee.shop.entity.Payment;
import com.coffee.shop.enums.PaymentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByPaymentStateAndCreatedBetween(PaymentState paymentState, LocalDate start, LocalDate end);
}
