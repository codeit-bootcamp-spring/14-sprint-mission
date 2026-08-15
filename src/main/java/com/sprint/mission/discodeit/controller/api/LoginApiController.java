package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.auth.LoginRequestDto;
import com.sprint.mission.discodeit.dto.auth.LoginResponseDto;
import com.sprint.mission.discodeit.service.application.auth.AuthApplicationService;
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
@RequestMapping("/api/login")
public class LoginApiController {

    private final AuthApplicationService authApplicationService;

    @PostMapping
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequest
    ) {
        LoginResponseDto loginResponse = authApplicationService.login(loginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loginResponse);
    }
}
