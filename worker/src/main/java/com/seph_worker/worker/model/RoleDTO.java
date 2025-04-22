package com.seph_worker.worker.model;


import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
    private String name;
    private String description;
    private Integer parentId;
    private Integer permissionId;
    private List<Integer> modulesId;
}
