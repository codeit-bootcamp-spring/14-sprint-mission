package com.sprint.mission.discodeit.adapter.in.web.controller.auth;

import com.sprint.mission.discodeit.adapter.in.web.controller.user.dto.UserDto;
import com.sprint.mission.discodeit.core.application.user.port.in.UserUseCase;
import com.sprint.mission.discodeit.core.config.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final UserUseCase useCase;

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        String tokenValue = csrfToken.getToken();
        log.debug("CSRF 토큰 요청:{}", tokenValue);

        return ResponseEntity
                .status(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
                .build();
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserBySessionId(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return UserDto.from(userDetails.getUser().withOnline(true));
    }

    @PutMapping("/role")
    public UserDto updateUserRole(@Valid @RequestBody UserRoleUpdateRequest request) {
        return UserDto.from(useCase.updateUserRole(
                request.userId(),
                request.newRole()
        ));
    }
}
