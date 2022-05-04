package com.coffee.repository;

import com.coffee.shop.security.entity.Role;
import com.coffee.shop.security.entity.User;
import com.coffee.shop.security.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindUserByUsername() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testUser@gmail.com");
        user.setPassword("pass");
        Set<Role> roles = new HashSet<>();
        user.setRoles(roles);
        this.entityManager.persistAndFlush(user);
        User result = this.userRepository.findByUsername(user.getUsername()).get();
        assertThat(user.getEmail()).isEqualTo(result.getEmail());
    }

    @Test
    public void testExistUserByEmail() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testUser@gmail.com");
        user.setPassword("pass");
        Set<Role> roles = new HashSet<>();
        user.setRoles(roles);
        this.entityManager.persistAndFlush(user);
        Boolean result = this.userRepository.existsByEmail(user.getEmail());
        assertThat(true).isEqualTo(result);
    }
}
