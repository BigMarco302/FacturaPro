package com.seph_worker.worker.model;


import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private String name;
    private String password;
    private String user;
    private String area;
    private String task;
    private List<Integer> roles;
    private List<Integer> extras;
    private String principal;
    private Integer organizationId;
    private Boolean active;
    public List<Integer> notifications;

}
