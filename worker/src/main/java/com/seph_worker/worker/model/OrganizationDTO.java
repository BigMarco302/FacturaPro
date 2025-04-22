package com.seph_worker.worker.model;


import lombok.Data;

@Data
public class OrganizationDTO {

    private String uri;
    private String nombre;
    private String razonSocial;
    private String rfc;
    private String regimenFiscal;
    private Integer folioInicio;

    private String passwordKey;

    private String colonia;
    private String municipio;
    private String estado;
    private Integer codigoPostal;
    private String calle;
    private String interior;
    private String exterior;
}
