package com.coffee.shop.entity;

import com.coffee.shop.enums.InviteState;
import com.coffee.shop.security.entity.User;

import javax.persistence.*;

@Entity
@Table(name = "subscribes")
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne()
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private InviteState inviteState;

    public Subscribe() {
    }

    public Subscribe(Long id, Subscription subscription, User user, InviteState inviteState) {
        Id = id;
        this.subscription = subscription;
        this.user = user;
        this.inviteState = inviteState;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public InviteState getInviteState() {
        return inviteState;
    }

    public void setInviteState(InviteState inviteState) {
        this.inviteState = inviteState;
    }

    @Override
    public String toString() {
        return "Subscribe{" +
                "Id=" + Id +
                ", subscription=" + subscription +
                ", user=" + user +
                ", inviteState=" + inviteState +
                '}';
    }
}
