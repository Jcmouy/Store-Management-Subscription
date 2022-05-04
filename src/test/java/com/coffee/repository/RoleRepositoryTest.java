package com.coffee.repository;

import com.coffee.shop.security.entity.Role;
import com.coffee.shop.security.entity.URole;
import com.coffee.shop.security.repository.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void testShouldFindAdminRole() {
        Optional<Role> resultOpt = this.roleRepository.findByName(URole.ROLE_ADMIN);
        if (resultOpt.isPresent()){
            Role result = resultOpt.get();
            assertThat(URole.ROLE_ADMIN.name()).isEqualTo(result.getName().name());
        }
    }

    @Test
    public void testShouldFindUserRole() {
        Optional<Role> resultOpt = this.roleRepository.findByName(URole.ROLE_USER);
        if (resultOpt.isPresent()){
            Role result = resultOpt.get();
            assertThat(URole.ROLE_USER.name()).isEqualTo(result.getName().name());
        }
    }

    @Test
    public void testShouldFindExternalRole() {
        Optional<Role> resultOpt = this.roleRepository.findByName(URole.ROLE_EXTERNAL);
        if (resultOpt.isPresent()){
            Role result = resultOpt.get();
            assertThat(URole.ROLE_EXTERNAL.name()).isEqualTo(result.getName().name());
        }
    }

}
