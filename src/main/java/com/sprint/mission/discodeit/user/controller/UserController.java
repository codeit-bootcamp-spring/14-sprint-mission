package com.sprint.mission.discodeit.user.controller;

import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.user.application.UserService;
import com.sprint.mission.discodeit.user.application.UserStatusService;
import com.sprint.mission.discodeit.user.dto.userStatus.UserStatusUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserDto create(@RequestPart(value = "userCreateRequest") UserCreateRequestDto request,
                          @RequestPart(value = "profile", required = false) MultipartFile profile){
        return userService.create(request, profile);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserDto update(@PathVariable UUID userId,
                       @RequestPart("userUpdateRequest") UserUpdateRequestDto request,
                       @RequestPart(value = "profile", required = false) MultipartFile profile){
        return userService.update(userId, request, profile);
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable UUID userId){
        userService.delete(userId);
    }

    @GetMapping
    public List<UserDto> findAll(){
        return userService.findAll();
    }

    @PatchMapping("/{userId}/userStatus")
    public UserStatusDto statusUpdate(@PathVariable UUID userId,
                                      @RequestBody UserStatusUpdateRequestDto request){

        return userStatusService.updateByUserId(userId, request);
    }



}
