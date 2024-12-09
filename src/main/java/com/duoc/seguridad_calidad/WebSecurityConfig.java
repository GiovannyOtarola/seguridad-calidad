package com.duoc.seguridad_calidad;

import com.duoc.seguridad_calidad.provider.CustomAuthenticationProvider;
import com.duoc.seguridad_calidad.provider.CustomAuthenticationSuccessHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig {

     public final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

     public WebSecurityConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, CustomAuthenticationProvider authProvider) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .authorizeHttpRequests(requests -> requests
            .requestMatchers("/", "/home", "/buscar","/login","/registro","/images/**.jpg").permitAll()
            .requestMatchers(HttpMethod.POST, "/registro").permitAll()
            .requestMatchers("/admin","/admin/**").hasRole("ADMIN")
            .requestMatchers("/recetas/**").authenticated() 
            .requestMatchers("/**.css").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/home", true)
            .failureUrl("/login?error=true")
            .permitAll()
            .successHandler(customAuthenticationSuccessHandler)
        )
        .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    
    
}
