package com.seph_worker.worker.security;


import com.seph_worker.worker.core.entity.RoleModuleUser.User;
import com.seph_worker.worker.repository.UserRoleModule.RoleRepository;
import com.seph_worker.worker.repository.UserRoleModule.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository rolRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User usuario = userRepository.findOneByUser(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario: " + username + " no existe"));



        List<Integer> roles = userRepository.getRoles(usuario.getId());
        roles.forEach(data-> {
            if(data == null)
            throw new UsernameNotFoundException("Su cuenta no cuenta con un rol");
        });

        if (!usuario.getActive()) {
            throw new UsernameNotFoundException("Cuenta deshabilitada");
        }

        return new UserDetailsImpl(usuario,roles,rolRepository);
    }
}
