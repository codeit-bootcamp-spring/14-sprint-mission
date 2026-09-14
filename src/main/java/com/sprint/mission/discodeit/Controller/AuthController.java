package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.dto.LoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.service.basic.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @RequestMapping(method = RequestMethod.POST,value = "/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginRequestDto request) {
        UserResponseDto response = authService.login(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
