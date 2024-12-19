package com.javaee.springbootodo.security;

import com.javaee.springbootodo.entity.UserEntity;
import com.javaee.springbootodo.repository.UserRepository;
import com.javaee.springbootodo.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    @Resource
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 配置 UserDetailsService
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserEntity userEntity = userService.findById(Integer.parseInt(username));
            if (userEntity == null) {
                throw new UsernameNotFoundException(String.format("User %s not found", username));
            }
            return new User(String.valueOf(userEntity.getId()), userEntity.getPassword(), new ArrayList<>());
        };
    }

    // 配置 AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        // 使用 HttpSecurity 获取 AuthenticationManagerBuilder
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        // 配置 userDetailsService 和 passwordEncoder
        authenticationManagerBuilder
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder);

        // 返回构建的 AuthenticationManager
        return authenticationManagerBuilder.build();
    }

    // 配置 HttpSecurity
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/login", "/user/register","/swagger-ui/**", "/swagger-ui.html", "/error").permitAll()  // 放行/error
                        .anyRequest().authenticated()
                )
//                .exceptionHandling(exceptionHandling ->
//                        exceptionHandling
//                                .accessDeniedHandler((request, response, accessDeniedException) -> {
//                                    // 访问拒绝时的自定义逻辑，重定向到/error
//                                    response.sendRedirect("/error");
//                                })
//                                .authenticationEntryPoint((request, response, authException) -> {
//                                    // 未认证时的处理逻辑
//                                    response.sendRedirect("/login");  // 或者你可以返回401错误
//                                })
//                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

}