package com.seph_worker.worker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seph_worker.worker.core.dto.WebServiceResponse;
import com.seph_worker.worker.core.entity.RoleModuleUser.User;
import com.seph_worker.worker.core.entity.organizations.Organization;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Hibernate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AuthCredentials credentials;
        try {
            credentials = new ObjectMapper().readValue(request.getReader(), AuthCredentials.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse authentication request body", e);
        }

        UsernamePasswordAuthenticationToken userPAT = new UsernamePasswordAuthenticationToken(
                credentials.getUser(),
                credentials.getPassword(),
                Collections.emptyList()
        );

        return getAuthenticationManager().authenticate(userPAT);
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        String token = TokenUtils.createToken(userDetails.getUsername());

        // Configurar la respuesta con el token y JSON personalizado
        response.addHeader("Authorization",token);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.setContentType("application/json");


        response.setStatus(HttpServletResponse.SC_OK);
        User user = userDetails.getUserDTO();
        Map<String,Object> organization = userDetails.getOrganization();
        WebServiceResponse successResponse = new WebServiceResponse(true, "Autenticación exitosa", Map.of(
                "userId",user.getId(),
                "roles",userDetails.getALlRoles(),
                "organizationId",Integer.parseInt(organization.get("id").toString()),
                "nameOrganization",organization.get("name").toString(),
                "permisos",userDetails.permisos(),
                "config", user.getConfig().get("config")
        ));

        ObjectMapper mapper = new ObjectMapper();
        try (PrintWriter out = response.getWriter()) {
            mapper.writeValue(out, successResponse);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        WebServiceResponse errorResponse;

        if (failed instanceof UsernameNotFoundException) {
            errorResponse = new WebServiceResponse(false, failed.getMessage());
        } else if (failed instanceof BadCredentialsException) {
            errorResponse = new WebServiceResponse(false, "La contraseña es incorrecta");
        } else {
            errorResponse = new WebServiceResponse(false, "Los datos ingresados son incorrectos");
        }

        ObjectMapper mapper = new ObjectMapper();
        try (PrintWriter out = response.getWriter()) {
            mapper.writeValue(out, errorResponse);
        }
    }
}
