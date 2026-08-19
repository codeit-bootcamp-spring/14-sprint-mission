package com.sprint.mission.discodeit.auth.controller;

import com.sprint.mission.discodeit.auth.dto.LoginRequestDto;
import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    // 사용자 로그인
    @RequestMapping(method = RequestMethod.POST, value = "/api/auth/login")
    public ResponseEntity<UserResponseDto> login(
        @Valid @RequestBody LoginRequestDto loginRequestDto) {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(authService.login(loginRequestDto));
    }
}
