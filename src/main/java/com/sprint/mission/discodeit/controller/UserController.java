package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userdto.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.userdto.UserResponseDto;
import com.sprint.mission.discodeit.dto.userdto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.userstatusdto.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userstatusdto.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ser.bean.UnwrappingBeanSerializer;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public UserResponseDto create(@RequestBody UserCreateRequestDto dto) {
        UserResponseDto newUser = userService.createUser(dto);
        log.info("유저 생성 완료, 유저 이름: " + newUser.getName());
        return newUser;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserResponseDto> getList() {
        return userService.readAllUser();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public UserResponseDto update(@PathVariable UUID id,
                                  @RequestBody UserUpdateRequestDto dto) {
        UserResponseDto target = userService.updateUser(id, dto);
        return target;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public UserStatusResponseDto updateStatus(@PathVariable UUID id,
                                              @RequestBody UserStatusUpdateRequestDto dto) {
    return userStatusService.updateUserStatus(id,dto);
    }


}
