package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.LoginRequestDto;
import com.sprint.mission.discodeit.dto.auth.LoginResponseDto;
import com.sprint.mission.discodeit.service.application.auth.AuthApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginApiController {

    private final AuthApplicationService authApplicationService;

    @PostMapping(value = "/api/login")
    public LoginResponseDto login(
            @RequestBody LoginRequestDto loginRequest
    ) {
        return authApplicationService.login(loginRequest);
    }
}
