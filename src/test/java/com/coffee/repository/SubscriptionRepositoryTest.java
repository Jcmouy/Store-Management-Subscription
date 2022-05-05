package com.coffee.repository;

import com.coffee.shop.entity.Subscription;
import com.coffee.shop.enums.PlanTypes;
import com.coffee.shop.security.entity.Role;
import com.coffee.shop.security.entity.URole;
import com.coffee.shop.security.entity.User;
import com.coffee.shop.security.repository.PlanRepository;
import com.coffee.shop.security.repository.RoleRepository;
import com.coffee.shop.security.repository.SubscriptionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SubscriptionRepositoryTest {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PlanRepository planRepository;

    @Test
    public void testAddSubscription() {
        User user = createUser();
        Subscription subscription = createSubscription(user);
        Subscription result = this.subscriptionRepository.findById(subscription.getId()).get();
        assertThat(subscription.getId()).isEqualTo(result.getId());
    }

    private User createUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testUser@gmail.com");
        user.setPassword("pass");
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(URole.ROLE_USER).get());
        user.setRoles(roles);
        this.entityManager.persistAndFlush(user);
        return user;
    }

    private Subscription createSubscription(User user) {
        Subscription subscription = new Subscription();
        Set<User> users = new HashSet<>();
        users.add(user);
        subscription.setPlan(planRepository.findByPlanName(PlanTypes.FAMILY).get());
        subscription.setUsers(users);
        this.entityManager.persistAndFlush(subscription);
        return subscription;
    }
}
