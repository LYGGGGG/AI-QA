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
                .csrf(csrf -> csrf.disable())  // 禁用 CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/**",     // 放行接口
                                "/qa",         // ✅ 放行页面 /qa
                                "/css/**",     // ✅ 放行静态资源（如果你有）
                                "/js/**",
                                "/images/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable()); // 关闭默认登录页

        return http.build();
    }
}
