package com.seph_worker.worker.core.entity.RoleModuleUser;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seph_worker.worker.core.dto.MapToJsonConverter;
import com.seph_worker.worker.core.entity.organizations.Organization;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.util.Map;
@Setter
@Getter
@Entity
@Table(name="users")
@Where(clause = "deleted = 0")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;


    @Column(name = "config")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> config;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "user", nullable = false)
    private String user;

    @Basic
    @Column(name = "password", nullable = false)
    private String password;

    @Basic
    @Column(name = "area", nullable = false)
    private String area;

    @Basic
    @Column(name = "task", nullable = false)
    private String task;

    @Basic
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Basic
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Basic
    @Column(name = "organization_id", nullable = false)
    private Integer organizationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Organization organization;



}
