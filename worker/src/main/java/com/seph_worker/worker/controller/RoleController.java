package com.seph_worker.worker.controller;


import com.seph_worker.worker.core.dto.WebServiceResponse;
import com.seph_worker.worker.model.RoleByUserDTO;
import com.seph_worker.worker.model.RoleDTO;
import com.seph_worker.worker.service.RolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@CrossOrigin(origins = "*")
@RequestMapping("/roles")
public class RoleController {

    private final RolService rolService;

    @GetMapping("")
    public WebServiceResponse getAllRoles(){
        return new WebServiceResponse(rolService.getAllRoles());
    }

    @GetMapping("/role")
    public WebServiceResponse getRole(@RequestHeader Integer roleId){
        return new WebServiceResponse(rolService.getRole(roleId));
    }

    @GetMapping("/permisos")
    public WebServiceResponse getPermissions(){
        return new WebServiceResponse(rolService.getPermissions());
    }


    @GetMapping("/permisosById")
    public WebServiceResponse permisosById(
            @RequestHeader("permisoId") Integer permisoId){
        return new WebServiceResponse(rolService.permisosById(permisoId));
    }


    //POST-------------------------------------------------------->
    @PostMapping("")
    public WebServiceResponse addNewRole(@RequestBody RoleDTO roleDTO){
        return (rolService.addNewRole(roleDTO));
    }

    @PostMapping("/roleByUser")
    public WebServiceResponse roleByUser(
            @RequestBody List<Integer> rolesId,
            @RequestHeader("userId") Integer userId){
        return (rolService.addRoleByUser(rolesId,userId));
    }

    //UPDATE-------------------------------------------------------->

    @PatchMapping("")
    public WebServiceResponse updateRole(@RequestBody RoleDTO roleDTO, @RequestHeader Integer roleId){
        return (rolService.updateRole(roleDTO,roleId));
    }

    @PatchMapping("/softdeleted")
    public WebServiceResponse softdeleted(@RequestHeader Integer roleId){
        return (rolService.softdeleted(roleId));
    }
}
