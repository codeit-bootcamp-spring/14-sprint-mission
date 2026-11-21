package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.global.security.SpaCsrfTokenRequestHandler;
import com.sprint.mission.discodeit.user.security.LoginFailureHandler;
import com.sprint.mission.discodeit.user.security.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http,
    LoginSuccessHandler loginSuccessHandler, LoginFailureHandler loginFailureHandler) throws Exception {
    http.csrf(csrf -> csrf
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
    );

    http.formLogin(login -> login
        .loginProcessingUrl("/api/auth/login")
        .successHandler(loginSuccessHandler)
        .failureHandler(loginFailureHandler)
    );

    http.logout(logout -> logout
        .logoutUrl("/api/auth/logout")
        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
    );

    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/csrf-token", "/api/users", "/api/auth/login", "/api/auth/logout").permitAll()
        .requestMatchers("/api/**").authenticated()
        .anyRequest().permitAll()
    );

    http.exceptionHandling(ex -> ex
        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        .accessDeniedHandler(new AccessDeniedHandlerImpl())
    );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    var encoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
    encoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());
    return encoder;
  }

  @Bean
  public RoleHierarchy roleHierarchy(){
    return RoleHierarchyImpl.withDefaultRolePrefix()
      .role("ADMIN").implies("CHANNEL_MANAGER")
      .role("CHANNEL_MANAGER").implies("USER")
      .build();
  }

  @Bean
  public static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
    DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
    handler.setRoleHierarchy(roleHierarchy);
    return handler;
  }
}
