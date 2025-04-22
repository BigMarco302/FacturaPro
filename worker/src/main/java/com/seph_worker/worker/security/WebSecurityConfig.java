package com.seph_worker.worker.security;

import jakarta.servlet.Filter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@AllArgsConstructor
@EnableWebMvc
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JWTAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter();
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");
        return  http
                .addFilterBefore(customCorsFilter(), JWTAuthenticationFilter.class)
                .csrf().disable() // Deshabilitar CSRF, ya que estamos usando JWT
                .httpBasic().disable()

                .authorizeRequests()
                .requestMatchers("factura/image/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/login").permitAll() // Permitir acceso a /login
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Permitir OPTIONS para CORS
                .requestMatchers("/*.*").permitAll() // Permitir el acceso a archivos estáticos
                .anyRequest().authenticated() // Proteger cualquier otra solicitud
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sin estado
                .and()
                .addFilter(jwtAuthenticationFilter) // Añadir filtro de autenticación
                //.addFilter(alternativeJwtAuthenticationFilter) // Filtro para /login2
                .addFilterBefore(customCorsFilter(), JWTAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class) // Añadir filtro de autorización antes del filtro de autenticación
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(daoAuthenticationProvider()) // Usar DaoAuthenticationProvider
                .build();
    }

    @Bean
    public Filter customCorsFilter() {
        return new CorsFilter();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService); // Definir el servicio de UserDetails
        provider.setHideUserNotFoundExceptions(false); // Permitir que se lance UsernameNotFoundException
        provider.setPasswordEncoder(passwordEncoder()); // Definir el codificador de contraseñas
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usar BCrypt para codificar contraseñas
    }

}
