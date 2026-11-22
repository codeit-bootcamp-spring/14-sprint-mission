package com.sprint.mission.discodeit.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.global.security.JwtTokenProvider;
import com.sprint.mission.discodeit.user.application.UserApplicationService;
import com.sprint.mission.discodeit.user.web.dto.res.JwtResponse;
import com.sprint.mission.discodeit.user.web.dto.res.UserResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {
  private final UserApplicationService userApplicationService;
  private final JwtTokenProvider jwtTokenProvider;
  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    var principal = (DiscodeitUserDetails) authentication.getPrincipal();
    UUID userId = principal.getUser().id();

    UserResponseDTO user = userApplicationService.completeLogin(userId);
    String accessToken = jwtTokenProvider.generateAccessToken(user.id(), user.role());
    String refreshToken = jwtTokenProvider.generateRefreshToken(user.id());

    JwtResponse jwtResponse = JwtResponse.of(user, accessToken);

    Cookie cookie = new Cookie("REFRESH_TOKEN", refreshToken);
    cookie.setHttpOnly(true);

    response.addCookie(cookie);
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    objectMapper.writeValue(response.getOutputStream(), jwtResponse);
  }
}
