package com.sprint.mission.discodeit.config.security;

import com.sprint.mission.discodeit.common.exception.ErrorResponse;
import com.sprint.mission.discodeit.domain.user.UserExceptionType;
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
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        UserExceptionType userExceptionType = UserExceptionType.LOGIN_FAILED;
        int statusCode = userExceptionType.getHttpStatus().value();

        ErrorResponse errorResponse = ErrorResponse.of(
                Instant.now(),
                userExceptionType.toString(),
                userExceptionType.getDescription(),
                Map.of(),
                exception.getClass().getName(),
                statusCode
        );

        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
