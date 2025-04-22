package com.seph_worker.worker.core.entity.Notifications;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="user_notifications")
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "status", nullable = false)
    private Integer status;

    @Basic
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Basic
    @Column(name = "message_id", nullable = false)
    private Integer messageId;

    @Basic
    @Column(name = "subscription_user_id", nullable = false)
    private Integer subscriptionUserId;





}
