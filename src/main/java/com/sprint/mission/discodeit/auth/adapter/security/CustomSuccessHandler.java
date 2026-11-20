package com.sprint.mission.discodeit.auth.adapter.security;

import com.sprint.mission.discodeit.user.application.dto.UserDto;
import com.sprint.mission.discodeit.user.application.provided.query.UserFinder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

  private final UserFinder userFinder;
  private final JsonMapper jsonMapper;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
    UserDto userDto = userFinder.getById(principal.getUserId());

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    jsonMapper.writeValue(response.getOutputStream(), userDto);
  }
}
