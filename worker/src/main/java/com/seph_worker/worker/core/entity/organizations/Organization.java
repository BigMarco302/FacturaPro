package com.seph_worker.worker.core.entity.organizations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Setter
@Getter
@Entity
@Table(name="organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "uri", nullable = false)
    private String uri;

    @Basic
    @Column(name = "razon_social", nullable = false)
    private String razonSocial;

    @Basic
    @Column(name = "rfc", nullable = false)
    private String rfc;

    @Basic
    @Column(name = "regimen_fiscal", nullable = false)
    private String regimenFiscal;

    @Basic
    @Column(name = "folio", nullable = false)
    private Integer folio;

    @Basic
    @Column(name = "save", nullable = false)
    private Boolean save;

    @Basic
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Basic
    @Column(name = "fiscal_address_id", nullable = false)
    private Integer fiscalAddressId;

    @Basic
    @Column(name = "digital_seal_id", nullable = false)
    private Integer digitalSealId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fiscal_address_id", referencedColumnName = "id", insertable = false, updatable = false)
    private FiscalAddress fiscalAddress;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "digital_seal_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DigitalSeal digitalSeal;
}
