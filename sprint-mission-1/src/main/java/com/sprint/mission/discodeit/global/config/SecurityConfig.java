package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.global.exception.ExceptionType;
import com.sprint.mission.discodeit.global.security.SecurityErrorWriter;
import com.sprint.mission.discodeit.global.security.SpaCsrfTokenRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityErrorWriter errorWriter;

    @Bean
    // 핸들러는 Spring 인터페이스 타입으로 받아 global이 auth 구현 클래스에 의존하지 않게 한다
    public SecurityFilterChain filterChain(HttpSecurity http,
        AuthenticationSuccessHandler loginSuccessHandler,
        AuthenticationFailureHandler loginFailureHandler,
        SessionRegistry sessionRegistry,
        UserDetailsService userDetailsService,
        @Value("${discodeit.security.remember-me.key}") String rememberMeKey,
        @Value("${discodeit.security.remember-me.token-validity-seconds:1209600}") int rememberMeValiditySeconds)
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

        http.sessionManagement(session -> session
            .sessionConcurrency(concurrency -> concurrency
                .maximumSessions(1)
                // false: 새 로그인을 허용하고 기존 세션을 만료시킨다.
                // true는 로그아웃 없이 브라우저만 닫은 사용자가 세션 타임아웃까지 다시 로그인하지 못한다
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry)
                // 기본 전략은 상태 코드 없이(200) 평문을 써서 FE가 성공으로 처리한다. 401 JSON으로 응답한다
                .expiredSessionStrategy(event ->
                    errorWriter.write(event.getResponse(), ExceptionType.SESSION_EXPIRED))));

        // 세션이 없어도(타임아웃, 재배포) remember-me 쿠키로 다시 인증하고 새 세션을 만든다.
        // 쿠키는 username + 만료 시각 + 서명(key, 비밀번호 해시 포함)이라 key가 유출되면 위조할 수 있다
        http.rememberMe(rememberMe -> rememberMe
            .key(rememberMeKey)
            .rememberMeParameter("remember-me")
            .tokenValiditySeconds(rememberMeValiditySeconds)
            .userDetailsService(userDetailsService));

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

    // 사용자별 세션 목록. 동시 세션 제한, 권한 변경 시 세션 만료, 접속 여부 계산에 쓴다
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    // 세션이 사라질 때(로그아웃, 타임아웃) 이벤트를 발행해 SessionRegistry에서도 지워지게 한다
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
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
