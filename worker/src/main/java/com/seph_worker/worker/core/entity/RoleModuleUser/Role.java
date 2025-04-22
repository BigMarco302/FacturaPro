package com.seph_worker.worker.core.entity.RoleModuleUser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seph_worker.worker.core.dto.MapToJsonConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Setter
@Getter
@Entity
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "config")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, Object> config;

    @Basic
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Basic
    @Column(name = "parent_id")
    private Integer parentId;

    @Basic
    @Column(name = "permission_id")
    private Integer permissionoId;

}
