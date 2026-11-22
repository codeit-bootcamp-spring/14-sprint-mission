package com.sprint.mission.discodeit.user.web;

import com.sprint.mission.discodeit.user.application.AuthApplicationService;
import com.sprint.mission.discodeit.user.application.UserApplicationService;
import com.sprint.mission.discodeit.user.web.dto.req.UserRoleUpdateRequestDTO;
import com.sprint.mission.discodeit.user.web.dto.res.JwtResponse;
import com.sprint.mission.discodeit.user.web.dto.res.RefreshTokenResult;
import com.sprint.mission.discodeit.user.web.dto.res.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final UserApplicationService userApplicationService;
    private final AuthApplicationService authApplicationService;

    @GetMapping("/csrf-token")
    public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
      String token = csrfToken.getToken();
      log.info("CSRF 토큰 요청: {}", token);
      return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build();
    }

    @PutMapping("/role")
    public ResponseEntity<UserResponseDTO> roleUpdate(
        @Valid @RequestBody UserRoleUpdateRequestDTO request
    ){
      UserResponseDTO response = userApplicationService.updateUserRole(request);

      return ResponseEntity.status(HttpStatus.OK)
          .body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshAccessToken(
        @CookieValue(value = "REFRESH_TOKEN", required = false) String refreshToken
    ){
      RefreshTokenResult response = authApplicationService.refreshAccessToken(refreshToken);
      String cookie = ResponseCookie.from("REFRESH_TOKEN", response.refreshToken())
          .httpOnly(true)
          .build()
          .toString();

      return ResponseEntity.status(HttpStatus.OK)
          .header(HttpHeaders.SET_COOKIE, cookie)
          .body(response.jwtResponse());
    }
}
