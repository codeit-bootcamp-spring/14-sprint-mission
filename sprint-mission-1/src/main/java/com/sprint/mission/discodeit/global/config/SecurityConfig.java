package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.auth.security.LoginFailureHandler;
import com.sprint.mission.discodeit.auth.security.LoginSuccessHandler;
import com.sprint.mission.discodeit.global.exception.ExceptionType;
import com.sprint.mission.discodeit.global.security.SecurityErrorWriter;
import com.sprint.mission.discodeit.global.security.SpaCsrfTokenRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityErrorWriter errorWriter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
        LoginSuccessHandler loginSuccessHandler, LoginFailureHandler loginFailureHandler)
        throws Exception {
        http.csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()));

        http.formLogin(login -> login
            .loginProcessingUrl("/api/auth/login")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler));

        http.logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .logoutSuccessHandler(
                new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT)));

        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.GET, "/api/auth/csrf-token").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers("/api/auth/login", "/api/auth/logout").permitAll()
            // 공개 경로는 허용 목록으로 명시한다. 새 경로는 기본적으로 인증이 필요하다
            .requestMatchers(HttpMethod.GET, "/", "/index.html", "/favicon.ico", "/assets/**")
            .permitAll()
            .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .requestMatchers("/actuator/health/**", "/actuator/info").permitAll()
            .requestMatchers("/actuator/**").hasRole("ADMIN")
            .requestMatchers("/error").permitAll()
            .anyRequest().authenticated());

        http.exceptionHandling(exception -> exception
            .authenticationEntryPoint(
                (request, response, e) -> errorWriter.write(response, ExceptionType.AUTH_REQUIRED))
            .accessDeniedHandler(
                (request, response, e) -> errorWriter.write(response,
                    ExceptionType.ACCESS_DENIED)));

        return http.build();
    }

    // URL 인가와 @EnableMethodSecurity 모두 RoleHierarchy Bean을 자동으로 주입받는다
    // (Security 7에서 expressionHandler.setRoleHierarchy는 deprecated)
    @Bean
    static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
            .role("ADMIN").implies("CHANNEL_MANAGER")
            .role("CHANNEL_MANAGER").implies("USER")
            .build();
    }
}
