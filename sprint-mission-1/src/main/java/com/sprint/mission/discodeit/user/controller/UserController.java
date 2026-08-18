package com.sprint.mission.discodeit.user.controller;

import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    // 사용자 등록
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/users")
    public ResponseEntity<UserResponseDto> create(
        @Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.userCreate(userCreateRequestDto));
    }

    // 사용자 수정
    @RequestMapping(method = RequestMethod.PATCH, value = "/api/v1/users/{id}")
    public ResponseEntity<UserResponseDto> update(
        @PathVariable UUID id,
        @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.userUpdate(id, userUpdateRequestDto));
    }

    // 사용자 삭제
    @RequestMapping(method = RequestMethod.DELETE, value = "/api/v1/users/{id}")
    public void delete(
        @PathVariable UUID id) {
        userService.userDelete(id);
    }

    // 전체 사용자 조회
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/users")
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.findAll());
    }
}
