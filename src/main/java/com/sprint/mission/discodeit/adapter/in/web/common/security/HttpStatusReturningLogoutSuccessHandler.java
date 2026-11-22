package com.sprint.mission.discodeit.adapter.in.web.common.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class HttpStatusReturningLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                @Nullable Authentication authentication) {
        int statusCode = HttpStatus.NO_CONTENT.value();
        response.setStatus(statusCode);

        if (Objects.nonNull(authentication)) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            UUID userId = customUserDetails.getUserId();
            log.info("Logout Success: userId={}", userId);
        }
    }
}
