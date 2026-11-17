package com.sprint.mission.discodeit.adapter.in.controller.auth;

import com.sprint.mission.discodeit.adapter.in.controller.user.dto.UserDto;
import com.sprint.mission.discodeit.application.user.in.UserApplication;
import com.sprint.mission.discodeit.config.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping(value = "/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthApiController {

    private final UserApplication application;

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        String tokenValue = csrfToken.getToken();
        log.debug("CSRF 토큰 요청:{}", tokenValue);

        return ResponseEntity
                .status(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
                .header(HttpHeaders.SET_COOKIE, tokenValue)
                .build();
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserBySessionId(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userDetails.getUserDto();
    }

    @PutMapping("/role")
    public UserDto updateUserRole(@Valid @RequestBody UserRoleUpdateRequest request) {
        return application.updateUserRole(
                request.userId(),
                request.newRole()
        );
    }
}
