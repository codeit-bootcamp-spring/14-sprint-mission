package com.sprint.mission.discodeit.auth.controller;

import com.sprint.mission.discodeit.auth.application.AuthService;
import com.sprint.mission.discodeit.auth.dto.AuthLoginRequestDto;
import com.sprint.mission.discodeit.user.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login")
    public UserDto login(@Valid @RequestBody AuthLoginRequestDto request) {
        log.debug("로그인 요청: username={}", request.username());
        return authService.login(request);
    }
}
