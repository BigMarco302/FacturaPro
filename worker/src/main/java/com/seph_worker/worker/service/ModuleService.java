package com.seph_worker.worker.service;


import com.seph_worker.worker.core.dto.WebServiceResponse;
import com.seph_worker.worker.core.entity.RoleModuleUser.Modules;
import com.seph_worker.worker.core.entity.RoleModuleUser.RoleModule;
import com.seph_worker.worker.core.exception.ResourceNotFoundException;
import com.seph_worker.worker.model.ModulosDTO;
import com.seph_worker.worker.repository.UserRoleModule.ModuleRepository;
import com.seph_worker.worker.repository.UserRoleModule.RoleModuleRepository;
import com.seph_worker.worker.repository.UserRoleModule.RoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Service
@Log4j2
@AllArgsConstructor
public class ModuleService {

    private RoleModuleRepository roleModuleRepository;
    private ModuleRepository moduleRepository;
    private RoleRepository roleRepository;


    public List<Map<String,Object>> getAllModules(){
        return moduleRepository.getAllModules();
    }

    public Map<String, Object> getModule(Integer moduleId) {
        Map<String, Object> original = moduleRepository.getModuleById(moduleId);
        Map<String, Object> module = new HashMap<>(original); // <- ahora sí es mutable
        module.put("roles", moduleRepository.getRoleByModule(moduleId));
        return module;
    }


    @Transactional
    public WebServiceResponse addModule(ModulosDTO dto){
        try {
            Modules modulos = new Modules();
            modulos.setName(dto.getName());
            modulos.setPath(dto.getPath());
            modulos.setDescription(dto.getDescription());
            modulos.setVisible(dto.getVisible());
            modulos.setIconId(dto.getIconId());
            modulos.setDeleted(FALSE);
            modulos.setParentId(dto.getParentId());
            moduleRepository.save(modulos);

            if(dto.getParentId() == 0 ){
                Modules newModulos = moduleRepository.findById(modulos.getId()).orElse(null);
                newModulos.setParentId(modulos.getId());
                moduleRepository.save(newModulos);
            }

            if(!dto.getRolesId().isEmpty())
                saveAllModuleByRoles(dto.getRolesId(),modulos.getId());
            return new WebServiceResponse(true, "Se agrego correctamente el modulo", "id", modulos.getId());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error updating report: " + e.getMessage());
        }
    }
    private void saveAllModuleByRoles(List<Integer> rolesId, Integer moduleId){
        rolesId.forEach(roleId -> {
            roleRepository.findById(roleId).orElseThrow(()->new ResourceNotFoundException("No se encontro el rol"));
            RoleModule roleModule = new RoleModule();
            roleModule.setModuleId(moduleId);
            roleModule.setRoleId(roleId);
            roleModuleRepository.save(roleModule);
        });
    }

    @Transactional
    public WebServiceResponse updateModule(ModulosDTO dto, Integer moduleId) {

        Modules modulos = moduleRepository.findById(moduleId).orElseThrow(() -> new ResourceNotFoundException("No se encontro el modulo"));

        try {
            modulos.setName(dto.getName());
            modulos.setPath(dto.getPath());
            modulos.setDescription(dto.getDescription());
            modulos.setVisible(dto.getVisible());
            modulos.setIconId(dto.getIconId());
            modulos.setDeleted(FALSE);
            modulos.setParentId(dto.getParentId());


            if (dto.getParentId() == 0) {
                modulos.setParentId(moduleId);
            } else {
                modulos.setParentId(dto.getParentId());
            }
            moduleRepository.save(modulos);

            List<Map<String, Integer>> currentRoles = roleModuleRepository.getRolesByModule(moduleId);
            currentRoles.forEach(module -> {

                Integer roleId = module.get("role_id");
                if (!dto.getRolesId().contains(roleId)) {
                    softDeletedRoleModule(module.get("id"));
                }
                dto.getRolesId().remove(roleId);
            });
            saveRoleAndModule(dto.getRolesId(), moduleId);
            return new WebServiceResponse(true, "Se Actualizo correctamente el modulo", "id", modulos.getId());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Error updating report: " + e.getMessage());
        }

    }
    @Transactional
    public WebServiceResponse saveRoleAndModule(List<Integer> rolesId, Integer moduleId){
        rolesId.forEach(roleId -> {
            roleRepository.findById(roleId).orElseThrow(()->new ResourceNotFoundException("No se encontro el role"));
            RoleModule roleModule = new RoleModule();
            roleModule.setModuleId(moduleId);
            roleModule.setRoleId(roleId);
            roleModule.setDeleted(FALSE);
            roleModuleRepository.save(roleModule);
        });
        return new WebServiceResponse("OK");
    }
    private void softDeletedRoleModule(Integer id){
        RoleModule roleModule = roleModuleRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No se encontro el Rol"));
        roleModule.setDeleted(TRUE);
        roleModuleRepository.save(roleModule);
    }

    @Transactional
    public WebServiceResponse softdeleted(Integer moduleId){
        Modules module = moduleRepository.findById(moduleId).orElseThrow(()->new ResourceNotFoundException("No se encontrro el modulo"));
        module.setDeleted(TRUE);
        moduleRepository.save(module);
        return new WebServiceResponse("Se elimino correctamente el modulo");
    }
}
