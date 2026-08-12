package com.sprint.mission.discodeit.user.controller;

import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
    @RequestMapping(method = RequestMethod.POST, value = "/api/user")
    public UserResponseDto create(
        @Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        return userService.userCreate(userCreateRequestDto);
    }

    // 사용자 수정
    @RequestMapping(method = RequestMethod.PATCH, value = "/api/user/{id}")
    public UserResponseDto update(
        @PathVariable UUID id,
        @Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto) {
        return userService.userUpdate(id, userUpdateRequestDto);
    }

    // 사용자 삭제
    @RequestMapping(method = RequestMethod.DELETE, value = "/api/user/{id}/delete")
    public void delete(
        @PathVariable UUID id) {
        userService.userDelete(id);
    }

    // 전체 사용자 조회
    @RequestMapping(method = RequestMethod.GET, value = "/api/user/findAll")
    public List<UserResponseDto> findAll() {
        return userService.findAll();
    }
}
