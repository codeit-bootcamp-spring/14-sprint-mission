package com.sprint.mission.discodeit.auth.controller;

import com.sprint.mission.discodeit.auth.dto.AuthLoginRequestDto;
import com.sprint.mission.discodeit.auth.dto.AuthLoginResponseDto;
import com.sprint.mission.discodeit.auth.application.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login")
    public AuthLoginResponseDto login(@RequestBody AuthLoginRequestDto request){

        return authService.login(request);
    }
}
