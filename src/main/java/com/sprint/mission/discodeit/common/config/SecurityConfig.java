package com.sprint.mission.discodeit.common.config;

import com.sprint.mission.discodeit.auth.adapter.security.CustomSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


  /*
   * https://docs.spring.io/spring-security/reference/7.1/servlet/exploits/csrf.html#csrf-integration-javascript-spa
   *
   *csrf((csrf) -> csrf
   *   .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
   *    기본 저장소
   *	.csrfTokenRepository(CookieCsrfTokenRepository
   *  .withHttpOnlyFalse())
   * */

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, CustomSuccessHandler successHandler)
      throws Exception {
    http.csrf(csrf -> csrf.spa())
        .formLogin(form -> form.loginProcessingUrl("/api/auth/login")
            .successHandler(successHandler));
    
    return http.build();
  }

}
