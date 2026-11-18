package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.auth.security.LoginFailureHandler;
import com.sprint.mission.discodeit.auth.security.LoginSuccessHandler;
import com.sprint.mission.discodeit.global.security.SpaCsrfTokenRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security,
        LoginSuccessHandler loginSuccessHandler, LoginFailureHandler loginFailureHandler)
        throws Exception {
        security.csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()));

        security.formLogin(login -> login
            .loginProcessingUrl("/api/auth/login")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler));
        return security.build();
    }
}
