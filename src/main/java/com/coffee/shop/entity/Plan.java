package com.coffee.shop.entity;

import com.coffee.shop.enums.PlanTypes;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;

@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PlanTypes planName;

    @NumberFormat
    private Long numberOfCoffees;

    @NumberFormat
    private Double chargePerMonth;

    public Plan() {
    }

    public Plan(Integer id, PlanTypes planName, Long numberOfCoffees, Double chargePerMonth) {
        this.id = id;
        this.planName = planName;
        this.numberOfCoffees = numberOfCoffees;
        this.chargePerMonth = chargePerMonth;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PlanTypes getPlanName() {
        return planName;
    }

    public void setPlanName(PlanTypes planName) {
        this.planName = planName;
    }

    public Long getNumberOfCoffees() {
        return numberOfCoffees;
    }

    public void setNumberOfCoffees(Long numberOfCoffees) {
        this.numberOfCoffees = numberOfCoffees;
    }

    public Double getChargePerMonth() {
        return chargePerMonth;
    }

    public void setChargePerMonth(Double chargePerMonth) {
        this.chargePerMonth = chargePerMonth;
    }
}
