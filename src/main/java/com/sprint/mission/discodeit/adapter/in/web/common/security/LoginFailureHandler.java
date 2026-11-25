package com.sprint.mission.discodeit.adapter.in.web.common.security;

import com.sprint.mission.discodeit.adapter.in.web.common.exception.ErrorResponse;
import com.sprint.mission.discodeit.adapter.in.web.common.exception.ErrorResponseMapper;
import com.sprint.mission.discodeit.core.domain.user.UserExceptionType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ErrorResponse errorResponse = ErrorResponseMapper.toResponse(
                UserExceptionType.LOGIN_FAILED, Instant.now(), exception.getClass().getName()
        );

        response.setStatus(errorResponse.status());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
