package com.seph_worker.worker.core.entity.organizations;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Setter
@Getter
@Entity
@Table(name="digital_seals")
public class DigitalSeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "cer_seal", nullable = false)
    private String cer;

    @Basic
    @Column(name = "key_seal", nullable = false)
    private String key;

    @Basic
    @Column(name = "password", nullable = false)
    private String password;

    @Basic
    @Column(name = "config", nullable = false)
    private String config;

}
