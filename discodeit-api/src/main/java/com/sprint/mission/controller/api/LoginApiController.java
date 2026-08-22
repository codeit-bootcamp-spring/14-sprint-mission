package com.sprint.mission.controller.api;

import com.sprint.mission.application.auth.AuthApplicationService;
import com.sprint.mission.controller.dto.auth.LoginRequestDto;
import com.sprint.mission.controller.dto.user.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class LoginApiController {

    private final AuthApplicationService authApplicationService;

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequest
    ) {
        UserResponseDto loggedInUser = authApplicationService.login(loginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loggedInUser);
    }
}
