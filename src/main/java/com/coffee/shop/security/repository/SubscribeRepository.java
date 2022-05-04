package com.coffee.shop.security.repository;

import com.coffee.shop.entity.Subscribe;
import com.coffee.shop.enums.InviteState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe,Long> {

    Subscribe findSubscribeByUser_Username(String username);

    List<Subscribe> findAllByInviteStateAndUser_Username(InviteState inviteState, String username);

    Subscribe findBySubscription_IdAndUser_Username(Long id, String username);

}
