package com.seph_worker.worker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seph_worker.worker.core.dto.WebServiceResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.io.PrintWriter;

@AllArgsConstructor
@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7); // Quitar "Bearer "

            Object authorization;
             authorization = TokenUtils.getAuthorization(token);

            if (authorization instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken usernamePAT = (UsernamePasswordAuthenticationToken) authorization;
                SecurityContextHolder.getContext().setAuthentication(usernamePAT);

            } else if (authorization instanceof String) {

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                WebServiceResponse errorResponse = new WebServiceResponse(false, authorization.toString());
                ObjectMapper mapper = new ObjectMapper();
                try (PrintWriter out = response.getWriter()) {
                    mapper.writeValue(out, errorResponse);
                }
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
