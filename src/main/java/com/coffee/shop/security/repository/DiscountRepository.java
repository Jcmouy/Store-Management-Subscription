package com.coffee.shop.security.repository;

import com.coffee.shop.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends JpaRepository<Discount,Long> {

    Discount findDiscountByDiscountCode(String discountCode);

}
