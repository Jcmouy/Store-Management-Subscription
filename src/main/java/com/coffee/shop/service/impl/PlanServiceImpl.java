package com.coffee.shop.service.impl;

import com.coffee.shop.entity.Plan;
import com.coffee.shop.enums.PlanTypes;
import com.coffee.shop.exception.ObjectNotFoundException;
import com.coffee.shop.security.repository.PlanRepository;
import com.coffee.shop.service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;

    @Autowired
    public PlanServiceImpl(PlanRepository planRepository){
        this.planRepository = planRepository;
    }

    @Override
    public Plan getPlanByPlanName(String planName) {
        log.info("Getting plan by planName: {}", planName);
        Optional<Plan> plan = planRepository.findByPlanName(PlanTypes.valueOf(planName));
        if (plan.isPresent()){
            return plan.get();
        }
        throw new ObjectNotFoundException("Could not found " + planName +
                " in the database!");
    }

}
