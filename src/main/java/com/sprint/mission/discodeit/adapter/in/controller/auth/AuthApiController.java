package com.sprint.mission.discodeit.adapter.in.controller.auth;

import com.sprint.mission.discodeit.adapter.in.controller.auth.dto.LoginRequestDto;
import com.sprint.mission.discodeit.adapter.in.controller.user.dto.UserDto;
import com.sprint.mission.discodeit.application.user.in.AuthApplication;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping(value = "/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthApiController {
    private final AuthApplication authApplication;

    @PostMapping(value = "/login")
    public UserDto login(@Valid @RequestBody LoginRequestDto requestDto) {
        return authApplication.login(requestDto.username(), requestDto.password());
    }

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        String tokenValue = csrfToken.getToken();
        log.debug("CSRF 토큰 요청:{}", tokenValue);

        return ResponseEntity
                .status(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
                .header(HttpHeaders.SET_COOKIE, tokenValue)
                .build();
    }
}
