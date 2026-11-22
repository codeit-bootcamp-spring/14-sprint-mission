package com.sprint.mission.discodeit.global.security;

import com.sprint.mission.discodeit.global.security.exception.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String header = request.getHeader("Authorization");

    if (header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);      // 다음 필터 호출 (로그인)
      return;
    }

    String token = header.substring(7);

    try {
      JwtPrincipal principal = jwtTokenProvider.getPrincipalFromAccessToken(token);

      // 인증완료 > 인증서 시큐리티 컨텍스트 홀더 내부 저장
      var authentication = new UsernamePasswordAuthenticationToken(
          principal,
          null,
          List.of(new SimpleGrantedAuthority("ROLE_" + principal.role().name()))
      );
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (InvalidTokenException e) {
      // 토큰이 유효하지 않은 경우
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    filterChain.doFilter(request, response);
  }
}
