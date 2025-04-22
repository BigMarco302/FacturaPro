package com.seph_worker.worker.service;

import com.seph_worker.worker.core.dto.WebServiceResponse;
import com.seph_worker.worker.core.entity.RoleModuleUser.Modules;
import com.seph_worker.worker.core.entity.RoleModuleUser.User;
import com.seph_worker.worker.core.entity.RoleModuleUser.UserRole;
import com.seph_worker.worker.core.exception.ResourceNotFoundException;
import com.seph_worker.worker.model.*;
import com.seph_worker.worker.repository.Organizations.OrganizationRepository;
import com.seph_worker.worker.repository.UserRoleModule.ModuleRepository;
import com.seph_worker.worker.repository.UserRoleModule.UserRepository;
import com.seph_worker.worker.repository.UserRoleModule.UserRoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Service
@Log4j2
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private ModuleRepository moduleRepository;
    private UserRoleRepository userRoleRepository;
    private OrganizationRepository organizationRepository;
    private RolService rolService;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro al usuario"));
    }

    public List<Map<String, Object>> getCredentialsByUser(Integer userId) {
        List<Map<String, Object>> modules = new ArrayList<>();
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("No se encontro al usuario"));
       List<Integer> extras = (List<Integer>) user.getConfig().get("extras");
       List<Integer> rolIds = userRepository.getRolesIdByUser(userId);
        if (extras == null) {
            extras = new ArrayList<>();
        }
        Modules module = moduleRepository.findByName("Salir");
        if(module != null){
            extras.add(module.getId());
        }
        modules.addAll(userRepository.getCredentialsByUser(rolIds));
        modules.addAll(userRepository.getCredentialsByModule(extras));

        return  modules;
    }

    @Transactional
    public WebServiceResponse addUser(UserDTO dto){

        try {
            User newUser = new User();
            Map<String,Object> config = new HashMap<>();
            config.put("principal", dto.getPrincipal());
            config.put("extras", dto.getExtras());

            newUser.setConfig(Map.of("config", config));
            newUser.setName(dto.getName());
            newUser.setUser(dto.getUser());
            newUser.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
            newUser.setArea(dto.getArea());
            newUser.setTask(dto.getTask());
            newUser.setActive(dto.getActive());
            newUser.setDeleted(FALSE);
            if(dto.getOrganizationId() == 0 ){
                newUser.setOrganizationId(null);
            }else{
                organizationRepository.findById(dto.getOrganizationId()).orElseThrow(()-> new ResourceNotFoundException("No se encontro el organizacion"));
                newUser.setOrganizationId(dto.getOrganizationId());
            }
            userRepository.save(newUser);
//            if(!dto.getNotifications().isEmpty()) {
//                notificationsService.subcribeUserToNotification(newUser.getId(), dto.getNotifications());
//            }
            rolService.addRoleByUser(dto.getRoles(),newUser.getId());
            return new WebServiceResponse(true, "Se agrego correctamente el user", "id", newUser.getId());
        } catch (Exception e) {
            throw new RuntimeException("Error updating report: " + e.getMessage());
        }
    }

    @Transactional
    public WebServiceResponse softdeleted(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("No se encontro al usuario"));
        user.setDeleted(TRUE);
        userRepository.save(user);
        return new WebServiceResponse(true, "Se elimino el usuario: "+user.getName());
    }

    @Transactional
    public WebServiceResponse updateUser(UserDTO dto, Integer userId){
        User oldUser = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("No se encontro el usuario"));
        Map<String,Object> config = new HashMap<>();
        config.put("principal", dto.getPrincipal());
        config.put("extras", dto.getExtras());

        oldUser.setConfig(Map.of("config", config));
        oldUser.setName(dto.getName());
        oldUser.setArea(dto.getArea());
        oldUser.setTask(dto.getTask());
        oldUser.setActive(dto.getActive());
        organizationRepository.findById(dto.getOrganizationId()).orElseThrow(()-> new ResourceNotFoundException("No se encontro el organizacion"));
        oldUser.setOrganizationId(dto.getOrganizationId());


        List<Map<String,Integer>> currentRoles = userRoleRepository.getRolesByUser(userId);
        currentRoles.forEach(role->{
            Integer roleId = role.get("roleId");
            if(!dto.getRoles().contains(roleId)){
                softDeletedRoleUser(role.get("id"));
            }
            dto.getRoles().remove(roleId);
        });
        rolService.addRoleByUser(dto.getRoles(),userId);
        return new WebServiceResponse("Se actualizo correctamente el usuario");
    }

    private void softDeletedRoleUser(Integer roleUserId){
        UserRole userRole = userRoleRepository.findById(roleUserId).orElseThrow(()->new ResourceNotFoundException("No se encontro el Rol"));
        userRole.setDeleted(TRUE);
        userRoleRepository.save(userRole);
    }
}
