package com.seph_worker.worker.model;


import lombok.Data;

import java.util.List;

@Data
public class ModulosDTO {

    private String name;
    private String path;
    private String description;
    private Boolean visible;
    private Integer iconId;
    private Integer parentId;
    private List<Integer> rolesId;
}
