package com.sprint.mission.discodeit.auth.adapter.api;

import com.sprint.mission.discodeit.auth.application.dto.LoginRequest;
import com.sprint.mission.discodeit.auth.application.provided.AuthLogin;
import com.sprint.mission.discodeit.user.application.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthApiController {

  private final AuthLogin authLogin;

  @RequestMapping(method = RequestMethod.POST, value = "/api/auth/login")
  public ResponseEntity<UserDto> login(@RequestBody LoginRequest request) {
    UserDto response = authLogin.login(request);
    return ResponseEntity.ok(response);
  }

}
