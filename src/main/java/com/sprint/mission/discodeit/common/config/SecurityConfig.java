package com.sprint.mission.discodeit.common.config;

import com.sprint.mission.discodeit.auth.adapter.security.CustomFailureHandler;
import com.sprint.mission.discodeit.auth.adapter.security.CustomSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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
  public SecurityFilterChain filterChain(HttpSecurity http, CustomSuccessHandler successHandler,
      CustomFailureHandler failureHandler)
      throws Exception {

    http.csrf(csrf -> csrf.spa())
        .formLogin(form -> form.loginProcessingUrl("/api/auth/login")
            .successHandler(successHandler)
            .failureHandler(failureHandler))
        .logout(logout -> logout.logoutUrl("/api/auth/logout")
            .logoutSuccessHandler(
                new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT)));

    http.authorizeHttpRequests(auth ->
            auth.requestMatchers(HttpMethod.GET, "/api/auth/csrf-token").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users", "/api/auth/login", "/api/auth/logout")
                .permitAll()
                .requestMatchers("/api/**").authenticated().anyRequest().permitAll())
        .exceptionHandling(e -> e.defaultAuthenticationEntryPointFor(
            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
            PathPatternRequestMatcher.withDefaults().matcher("/api/**")));

    return http.build();
  }

  /*SimpleUrlLogoutSuccessHandler 의 onLogoutSuccess 서버가 targetUrl 지정
		this.redirectStrategy.sendRedirect(request, response, targetUrl);
	}
  * */

  /*
  * HttpStatusReturnLogoutSuccessHandler의 onLogoutSuccess 서버는 상태만 반환
 	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
			@Nullable Authentication authentication) throws IOException {
		response.setStatus(this.httpStatusToReturn.value());
		response.getWriter().flush();
	}

   */


}
