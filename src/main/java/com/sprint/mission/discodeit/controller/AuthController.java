package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.authdto.AuthCreateRequestDto;
import com.sprint.mission.discodeit.dto.userdto.UserResponseDto;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @RequestMapping(method = RequestMethod.POST, value = "/login" )
    public UserResponseDto login(@RequestBody AuthCreateRequestDto dto) {
        return authService.login(dto);
    }
}
