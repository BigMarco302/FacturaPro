package com.seph_worker.worker.core.entity.Notifications;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Setter
@Getter
@Table(name="messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "message", nullable = false)
    private String message;

    @Basic
    @Column(name = "title", nullable = false)
    private String title;

    @Basic
    @Column(name = "created_at", nullable = false)
    private LocalDateTime created;

    @Basic
    @Column(name = "notification_id", nullable = false)
    private Integer notificationId;

    @Basic
    @Column(name = "icon_id", nullable = false)
    private Integer iconId;
}
