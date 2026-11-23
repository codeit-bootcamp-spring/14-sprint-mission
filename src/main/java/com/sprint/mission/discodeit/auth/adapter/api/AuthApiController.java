package com.sprint.mission.discodeit.auth.adapter.api;

import com.sprint.mission.discodeit.auth.adapter.api.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.auth.adapter.security.CustomUserDetails;
import com.sprint.mission.discodeit.auth.application.dto.LoginRequest;
import com.sprint.mission.discodeit.auth.application.provided.AuthLogin;
import com.sprint.mission.discodeit.user.application.dto.UserDto;
import com.sprint.mission.discodeit.user.application.provided.command.UserRoleModifier;
import com.sprint.mission.discodeit.user.application.provided.query.UserFinder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthApiController {

  private final AuthLogin authLogin;
  private final UserFinder userFinder;
  private final UserRoleModifier roleModifier;

  @RequestMapping(method = RequestMethod.POST, value = "/api/auth/login")
  public ResponseEntity<UserDto> login(@RequestBody LoginRequest request) {
    UserDto response = authLogin.login(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/api/auth/me")
  public ResponseEntity<UserDto> me(
      @AuthenticationPrincipal CustomUserDetails principal
  ) {
    if (principal == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    UserDto userDto = userFinder.getById(principal.getUserId());
    return ResponseEntity.ok(userDto);
  }

  @PutMapping("/api/auth/role")
  public ResponseEntity<UserDto> updateRole(
      @RequestBody @Valid UserRoleUpdateRequest request
  ) {
    UserDto userDto = roleModifier.updateRole(request.userId(), request.role());
    return ResponseEntity.ok(userDto);
  }

  @GetMapping("/api/auth/csrf-token")
  public ResponseEntity<Void> csrfToken() {
    return ResponseEntity.noContent().build();
  }

}
