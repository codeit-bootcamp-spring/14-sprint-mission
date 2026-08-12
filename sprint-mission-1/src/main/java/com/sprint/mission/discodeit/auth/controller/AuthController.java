package com.sprint.mission.discodeit.auth.controller;

import com.sprint.mission.discodeit.auth.dto.LoginRequestDto;
import com.sprint.mission.discodeit.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    // 사용자 로그인
    @RequestMapping(method = RequestMethod.GET, value = "/api/auth")
    public void login(
        @Valid @RequestBody LoginRequestDto loginRequestDto) {
        authService.login(loginRequestDto);
    }
}
