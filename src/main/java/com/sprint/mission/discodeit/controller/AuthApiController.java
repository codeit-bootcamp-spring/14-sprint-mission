package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.LoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthApiController {
    private final BasicAuthService authService;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public UserDto login(@Valid @RequestBody LoginRequestDto requestDto) {
        return authService.login(requestDto.username(), requestDto.password());
    }
}
