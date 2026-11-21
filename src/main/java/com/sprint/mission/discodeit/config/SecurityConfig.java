package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final LoginSuccessHandler successHandler;
    private final LoginFailureHandler failureHandler;

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

        return http.build();
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
}
