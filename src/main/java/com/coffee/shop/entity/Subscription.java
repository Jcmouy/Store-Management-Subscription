package com.coffee.shop.entity;

import com.coffee.shop.security.entity.User;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "plan_plan_name")
    private Plan plan;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subscription_users",
            joinColumns = @JoinColumn(name = "subscription_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    public Subscription() {
    }

    public Subscription(Long id, Plan plan, Set<User> users) {
        this.id = id;
        this.plan = plan;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
