package com.sprint.mission.discodeit.auth.controller;

import com.sprint.mission.discodeit.auth.dto.UserRoleUpdateRequestDto;
import com.sprint.mission.discodeit.auth.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Auth", description = "Auth Api")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/api/auth/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
        log.debug("CSRF 토큰 요청: {}", csrfToken.getToken());
        return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build();
    }

    @GetMapping("/api/auth/me")
    public ResponseEntity<UserDto> me(
        @AuthenticationPrincipal DiscodeitUserDetails principal) {
        return ResponseEntity.ok(userService.findById(principal.getUserDto().id()));
    }

    @PutMapping("/api/auth/role")
    public ResponseEntity<UserDto> updateRole(
        @RequestBody @Valid UserRoleUpdateRequestDto request) {
        return ResponseEntity.ok(authService.updateRole(request));
    }
}
