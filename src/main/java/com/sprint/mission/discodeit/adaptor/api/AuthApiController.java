package com.sprint.mission.discodeit.adaptor.api;

import com.sprint.mission.discodeit.adaptor.dto.userDto.LoginRequest;
import com.sprint.mission.discodeit.application.AuthService;
import com.sprint.mission.discodeit.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthApiController {

  private final AuthService authService;

  @RequestMapping(method = RequestMethod.POST, value = "/api/auth/login")
  public ResponseEntity<User> login(@RequestBody LoginRequest request) {
    User response = authService.login(request);
    return ResponseEntity.ok(response);
  }

}
