package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userdto.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.userdto.UserResponseDto;
import com.sprint.mission.discodeit.dto.userdto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.userstatusdto.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userstatusdto.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;
    // URL 충돌방지를 위해 consumes로 미디어 타입 명시
    @RequestMapping(method = RequestMethod.POST, value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(@RequestBody UserCreateRequestDto dto) {
        UserResponseDto newUser = userService.createUser(dto);
        log.info("유저 생성 완료, 유저 이름: " + newUser.getName());
        return newUser;
    }

    // URL 충돌방지를 위해 consumes로 미디어 타입 명시
    @RequestMapping(method = RequestMethod.POST, value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createWithProfile(@RequestPart("userCreateRequest") UserCreateRequestDto dto,
                                             @RequestPart(value = "profile", required = false) MultipartFile profile) {
        UserResponseDto newUser = userService.createUser(dto);
        log.info("유저 생성 완료, 유저 이름: " + newUser.getName());
        return newUser;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<UserResponseDto> getList() {
        return userService.readAllUser();
    }
    // URL 충돌방지를 위해 consumes로 미디어 타입 명시
    @RequestMapping(method = RequestMethod.PATCH, value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDto update(@PathVariable UUID userId,
                                  @RequestBody UserUpdateRequestDto dto) {
        return userService.updateUser(userId, dto);
    }

    // URL 충돌방지를 위해 consumes로 미디어 타입 명시
    @RequestMapping(method = RequestMethod.PATCH, value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserResponseDto updateWithProfile(@PathVariable UUID userId,
                                             @RequestPart("userUpdateRequest") UserUpdateRequestDto dto,
                                             @RequestPart(value = "profile", required = false) MultipartFile profile) {
        return userService.updateUser(userId, dto);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID userId) {
        userService.deleteUser(userId);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{userId}/userStatus")
    public UserStatusResponseDto updateStatus(@PathVariable UUID userId,
                                               @RequestBody UserStatusUpdateRequestDto dto) {
        return userStatusService.updateUserStatusByUserId(userId, dto);
    }
}
