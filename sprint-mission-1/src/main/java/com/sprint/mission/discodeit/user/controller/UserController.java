package com.sprint.mission.discodeit.user.controller;

import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<UserResponseDto> create(
        @Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        return ResponseEntity.ok(userService.userCreate(userCreateRequestDto));
    }
}
