package com.seph_worker.worker.service;


import com.seph_worker.worker.core.dto.WebServiceResponse;
import com.seph_worker.worker.core.entity.RoleModuleUser.Role;
import com.seph_worker.worker.core.entity.RoleModuleUser.UserRole;
import com.seph_worker.worker.core.entity.RoleModuleUser.RoleModule;
import com.seph_worker.worker.core.exception.ResourceNotFoundException;
import com.seph_worker.worker.model.RoleByUserDTO;
import com.seph_worker.worker.model.RoleDTO;
import com.seph_worker.worker.repository.UserRoleModule.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Service
@Log4j2
@AllArgsConstructor
public class RolService {

    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private RoleModuleRepository roleModuleRepository;
    private UserRoleRepository userRoleRepository;
    private ModuleRepository moduleRepository;


    public List<Map<String,Object>> getAllRoles() {
        return roleRepository.getAllRolesAssigned();
    }


    public Map<String,Object> getRole(Integer roleId) {
        Map<String, Object> original = roleRepository.getRoleById(roleId);
        Map<String, Object> role = new HashMap<>(original); // <- ahora sí es mutable
        role.put("modules", roleRepository.getModulesByRole(roleId));
        return role;
    }

    @Transactional
    public WebServiceResponse addRoleByUser(List<Integer> dto, Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("No se encontro el usuario"));
        dto.forEach(role -> {
            roleRepository.findById(role)
                    .orElseThrow(()->new ResourceNotFoundException("No se encontro el rol"));
            UserRole newRoleUser = new UserRole();
            newRoleUser.setUserId(userId);
            newRoleUser.setRoleId(role);
            newRoleUser.setDeleted(FALSE);
            userRoleRepository.save(newRoleUser);
        });

        return new WebServiceResponse(true, "se asignaron correctamente los role", "Role assigned",dto);
    }

    @Transactional
    public WebServiceResponse addNewRole( RoleDTO dto){
        Role role = new Role();
        role.setName(dto.getName().toUpperCase());
        role.setDescription(dto.getDescription());
        role.setConfig(null);
        role.setDeleted(FALSE);
        role.setParentId(dto.getParentId());
        role.setPermissionoId(dto.getPermissionId());
        roleRepository.save(role);
        if(dto.getParentId() == 0 ){
            Role newRole = roleRepository.findById(role.getId()).get();
            newRole.setParentId( role.getId());
            roleRepository.save(newRole);
        }
        if(!dto.getModulesId().isEmpty())
            saveRoleAndModule(dto.getModulesId(), role.getId());

        return new WebServiceResponse("Se agrego correcatemnte el Rol: "+role.getName());
    }
    @Transactional
    public WebServiceResponse updateRole( RoleDTO dto,Integer roleId){

        Role role = roleRepository.findById(roleId)
                .orElseThrow(()->new ResourceNotFoundException("No se encontro el rol"));
        role.setName(dto.getName().toUpperCase());
        role.setDescription(dto.getDescription());
        role.setConfig(null);
        role.setDeleted(FALSE);
        role.setPermissionoId(dto.getPermissionId());
        if(dto.getParentId() == 0 ){
            role.setParentId(roleId);
        }else{
            role.setParentId(dto.getParentId());
        }
        roleRepository.save(role);

        List<Map<String,Integer>> currentRoles = roleModuleRepository.getModulesByRole(roleId);
        currentRoles.forEach(module->{
            Integer moduleId = module.get("module_id");
            if(!dto.getModulesId().contains(moduleId)){
                softDeletedRoleModule(module.get("id"));
            }
            dto.getModulesId().remove(moduleId);
        });
        saveRoleAndModule(dto.getModulesId(), roleId);
        return new WebServiceResponse("Se Actualizo correcatemnte el Rol: "+role.getName());
    }
    private void softDeletedRoleModule(Integer id){
        RoleModule roleModule = roleModuleRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No se encontro el Rol"));
        roleModule.setDeleted(TRUE);
        roleModuleRepository.save(roleModule);
    }

    @Transactional
    public WebServiceResponse saveRoleAndModule(List<Integer> modulesId, Integer roleId){
        modulesId.forEach(id -> {
            moduleRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No se encontro el modulo"));
            RoleModule roleModule = new RoleModule();
            roleModule.setModuleId(id);
            roleModule.setRoleId(roleId);
            roleModule.setDeleted(FALSE);
            roleModuleRepository.save(roleModule);
        });
        return new WebServiceResponse("OK");
    }

    public List<Map<String,Object>> getPermissions (){
        return moduleRepository.getPermissions().stream().map(data->{
            Map<String,Object> map = new HashMap<>(data);
            map.put("auto",map.get("auto").toString().equals("1"));
            map.put("eliminar",map.get("eliminar").toString().equals("1"));
            map.put("agregar",map.get("agregar").toString().equals("1"));
            map.put("editar",map.get("editar").toString().equals("1"));
            return map;
        }).collect(Collectors.toList());
    }

    public Map<String, Object> permisosById(Integer permisoId) {
        Map<String, Byte> permiso = roleRepository.getPermiso(permisoId);
        if(permiso.isEmpty())throw new ResourceNotFoundException("No se encontro el permiso");
        Map<String, Object> permiso2 = new HashMap<>();

        permiso2.put("editar", permiso.get("editar") == 1);
        permiso2.put("autorizar", permiso.get("auto") == 1);
        permiso2.put("agregar", permiso.get("agregar") == 1);
        permiso2.put("eliminar", permiso.get("eliminar") == 1);

        return permiso2;
    }

    @Transactional
    public WebServiceResponse softdeleted(Integer id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontro el rol"));
        role.setDeleted(TRUE);
        roleRepository.save(role);
        return new WebServiceResponse("Se elimino correcatemnte el Rol: "+role.getName());
    }

}
