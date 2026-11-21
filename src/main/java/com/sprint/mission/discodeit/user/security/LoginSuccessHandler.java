package com.sprint.mission.discodeit.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.user.application.UserApplicationService;
import com.sprint.mission.discodeit.user.web.dto.res.UserResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserApplicationService userApplicationService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        DiscodeitUserDetails principal = (DiscodeitUserDetails) authentication.getPrincipal();
        UserResponseDTO user = userApplicationService.completeLogin(principal.getUser().userId());

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getOutputStream(), user);
    }
}
