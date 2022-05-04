package com.coffee.shop.security.repository;

import com.coffee.shop.entity.Plan;
import com.coffee.shop.enums.PlanTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan,Integer> {

    Optional<Plan> findByPlanName(PlanTypes planName);
}
