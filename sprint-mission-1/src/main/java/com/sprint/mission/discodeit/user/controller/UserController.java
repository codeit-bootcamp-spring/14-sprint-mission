package com.sprint.mission.discodeit.user.controller;

import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserDto;
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
    @RequestMapping(method = RequestMethod.POST, value = "/api/users")
    public ResponseEntity<UserDto> create(
        @Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userService.userCreate(userCreateRequestDto));
    }

    // 사용자 수정
    @RequestMapping(method = RequestMethod.PATCH, value = "/api/users/{userId}")
    public ResponseEntity<UserDto> update(
        @PathVariable UUID userId,
        @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.userUpdate(userId, userUpdateRequestDto));
    }

    // 사용자 삭제
    @RequestMapping(method = RequestMethod.DELETE, value = "/api/users/{userId}")
    public void delete(
        @PathVariable UUID userId) {
        userService.userDelete(userId);
    }

    // 전체 사용자 조회
    @RequestMapping(method = RequestMethod.GET, value = "/api/users")
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.findAll());
    }
}
