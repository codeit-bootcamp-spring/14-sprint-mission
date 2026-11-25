package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.common.ErrorResponse;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final LoginSuccessHandler successHandler;
    private final LoginFailureHandler failureHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()));
        http.formLogin(login -> login
                .loginProcessingUrl("/api/auth/login")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
            );
        http.logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
            .deleteCookies("JSESSIONID")
        );
        http.authorizeHttpRequests(auth -> {
            /* csrf 토큰 발급 */
            auth.requestMatchers(HttpMethod.GET, "/api/auth/csrf-token").permitAll();
            /* 회원가입 */
            auth.requestMatchers(HttpMethod.POST, "/api/users").permitAll();
            /* 로그인 */
            auth.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll();
            /* 로그아웃 */
            auth.requestMatchers(HttpMethod.POST, "/api/auth/logout").permitAll();
            /* api가 아닌 요청 */
            auth.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll();
            auth.requestMatchers("/actuator/**").permitAll();
            /* 프론트앤드 */
            auth.requestMatchers("/", "/index.html", "/assets/**", "/favicon.ico").permitAll();
            auth.requestMatchers("/*.js", "/*.css", "/*.png", "/*.svg").permitAll();
            /* 그외 나머지는 인증이 필요함 */
            auth.anyRequest().authenticated();
            }
        );
        http.exceptionHandling(ex -> ex
            .authenticationEntryPoint((req, res, e) -> writeErrorResponse(res, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", e))
            .accessDeniedHandler((req, res, e) -> writeErrorResponse(res, HttpStatus.FORBIDDEN, "ACCESS_DENIED", e))
        );
        return http.build();
    }

    private void writeErrorResponse(HttpServletResponse response, HttpStatus status, String code, Exception e)
        throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(
            Instant.now(),
            code,
            e.getMessage(),
            Map.of(),
            e.getClass().getSimpleName(),
            status.value()
        );
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationRunner initAdmin(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        @Value("${discodeit.admin.username:admin}") String username,
        @Value("${discodeit.admin.email:admin@discodeit.com}") String email,
        @Value("${discodeit.admin.password:admin1234}") String password
    ) {
        return args -> {
            if (userRepository.existsByUsername(username)) {
                return;
            }
            User admin = new User(username, email, passwordEncoder.encode(password), null);
            admin.updateRole(Role.ADMIN);
            userRepository.save(admin);
        };
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl.Builder builder = RoleHierarchyImpl.withDefaultRolePrefix();
        builder.role("ADMIN").implies("CHANNEL_MANAGER");
        builder.role("CHANNEL_MANAGER").implies("USER");
        return builder.build();
    }

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }
}
