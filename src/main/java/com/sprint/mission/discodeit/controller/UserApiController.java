package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserStatusDto;
import com.sprint.mission.discodeit.dto.user.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequestMapping(value = "/api/users")
@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final BasicUserService userService;
    private final BasicUserStatusService userStatusService;

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public UserDto create(@Valid @RequestPart(required = true) UserCreateRequest userCreateRequest,
                          @RequestPart(required = false) MultipartFile profile) {
        return userService.createAccount(
                userCreateRequest.username(),
                userCreateRequest.email(),
                userCreateRequest.password(),
                profile
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PATCH, value = "/{userId}")
    public UserDto update(@PathVariable UUID userId,
                          @Valid @RequestPart UserUpdateRequest userUpdateRequest,
                          @RequestPart(required = false) MultipartFile profile) {
        return userService.updateUser(
                userId,
                userUpdateRequest.newUsername(),
                userUpdateRequest.newEmail(),
                userUpdateRequest.newPassword(),
                profile
        );
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    public void delete(@PathVariable UUID userId) {
        userService.deleteAccount(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET)
    public List<UserDto> findAll() {
        return userService.getAllUsers();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public UserDto read(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PATCH, value = "/{userId}/userStatus")
    public UserStatusDto updateUserStatusByUserId(@PathVariable UUID userId,
                                                  @Valid @RequestBody UserStatusUpdateRequest request) {
        return userStatusService.updateByUserId(userId, request.newLastActiveAt());
    }
}
