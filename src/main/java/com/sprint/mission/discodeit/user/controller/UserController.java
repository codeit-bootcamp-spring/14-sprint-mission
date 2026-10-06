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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
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
        log.debug("사용자 생성 요청 - username : {}, hasProfile : {}", request.username(), profile != null);
        return userService.create(request, profile);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UserDto update(@PathVariable UUID userId,
                       @RequestPart("userUpdateRequest") UserUpdateRequestDto request,
                       @RequestPart(value = "profile", required = false) MultipartFile profile){
        log.debug("사용자 수정 요청 - userId : {}, hasProfile : {}", userId, profile != null);
        return userService.update(userId, request, profile);
    }

    @DeleteMapping(value = "/{userId}")
    public void delete(@PathVariable UUID userId){
        log.debug("사용자 삭제 요청 - userId : {}", userId);
        userService.delete(userId);
    }

    @GetMapping
    public List<UserDto> findAll(){
        log.debug("사용자 목록 조회 요청");
        return userService.findAll();
    }

    @PatchMapping("/{userId}/userStatus")
    public UserStatusDto statusUpdate(@PathVariable UUID userId,
                                      @RequestBody UserStatusUpdateRequestDto request){
        log.debug("사용자 상태 수정 요청 - userId : {}", userId);
        return userStatusService.updateByUserId(userId, request);
    }



}
