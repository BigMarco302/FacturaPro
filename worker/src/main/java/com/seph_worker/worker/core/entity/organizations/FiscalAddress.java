package com.seph_worker.worker.core.entity.organizations;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Setter
@Getter
@Entity
@Table(name="fiscal_address")
public class FiscalAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "colonia", nullable = false)
    private String colonia;

    @Basic
    @Column(name = "municipio", nullable = false)
    private String municipio;

    @Basic
    @Column(name = "estado", nullable = false)
    private String estado;

    @Basic
    @Column(name = "codigo", nullable = false)
    private Integer codigo;

    @Basic
    @Column(name = "calle", nullable = false)
    private String calle;

    @Basic
    @Column(name = "interior", nullable = false)
    private String interior;

    @Basic
    @Column(name = "exterior", nullable = false)
    private String exterior;


}
