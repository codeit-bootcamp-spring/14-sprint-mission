package com.sprint.mission.discodeit.auth.adapter.security;

import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import com.sprint.mission.discodeit.common.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public class CustomFailureHandler implements AuthenticationFailureHandler {

  private final JsonMapper jsonMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    if (exception instanceof AuthenticationServiceException) {
      throw new ServletException("서버 오류가 발생했습니다.", exception);
    }

    ErrorResponse errorResponse = ErrorResponse.from(new DiscodeitRuntimeException(
        ErrorCode.LOGIN_FAILED));

    response.setStatus(errorResponse.status());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    jsonMapper.writeValue(response.getOutputStream(), errorResponse);
  }
}
