package com.ll.security_2025_01_06.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationFilter customAuthenticationFilter;

    @Bean
    public SecurityFilterChain baseSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests // 시큐리티는 상위 우선법이라 룰이 충돌할 경우 상위 룰이 우선시 된다.
                                .requestMatchers("/h2-console/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/*/posts/{id:\\d+}", "/api/*/posts", "/api/*/posts/{postId:\\d+}/comments")
                                .permitAll()
                                .requestMatchers("/api/*/members/login", "/api/*/members/join")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .headers(headers ->
                        headers.frameOptions(frameOptions ->
                                frameOptions.sameOrigin())
                )
                .csrf(csrf ->
                        csrf.disable()
                )
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
