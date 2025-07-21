package com.example.AI_QA.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // 禁用CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // ✅ 放行所有API请求
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable()); // 关闭默认登录页

        return http.build();
    }
}
