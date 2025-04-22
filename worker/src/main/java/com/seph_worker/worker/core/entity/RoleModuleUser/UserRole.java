package com.seph_worker.worker.core.entity.RoleModuleUser;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Setter
@Getter
@Entity
@Table(name="users_roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Basic
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Basic
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
}
