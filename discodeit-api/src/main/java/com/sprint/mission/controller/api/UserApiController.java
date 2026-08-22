package com.sprint.mission.controller.api;

import com.sprint.mission.application.user.UserApplicationService;
import com.sprint.mission.controller.dto.user.UserCreateRequest;
import com.sprint.mission.controller.dto.user.UserDto;
import com.sprint.mission.controller.dto.user.UserResponseDto;
import com.sprint.mission.controller.dto.user.UserUpdateRequest;
import com.sprint.mission.controller.dto.userstatus.UserStatusResponseDto;
import com.sprint.mission.controller.dto.userstatus.UserStatusUpdateRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
// 사용자 관리
// [x] 사용자를 등록할 수 있다.
// [x] 사용자 정보를 수정할 수 있다.
// [x] 사용자를 삭제할 수 있다.
// [x] 모든 사용자를 조회할 수 있다.
// [x] 사용자의 온라인 상태를 업데이트할 수 있다.
@RequestMapping("/api/users")
@Validated
public class UserApiController {

    private final UserApplicationService userApplicationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> create(
            @Valid @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
            @RequestPart(value = "profile", required = false) MultipartFile profileImage
    ) {
        UserResponseDto createdUser = userApplicationService.create(userCreateRequest, profileImage);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> update(
            @NotNull @PathVariable UUID userId,
            @Valid @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
            @RequestPart(value = "profile", required = false) MultipartFile profileImage
    ) {
        UserResponseDto updatedUser = userApplicationService.update(
                userId,
                userUpdateRequest,
                profileImage
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(
            @NotNull @PathVariable UUID userId
    ) {
        userApplicationService.delete(userId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> usersList = userApplicationService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(usersList);
    }

    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatusResponseDto> updateUserStatusByUserId(
            @NotNull @PathVariable UUID userId,
            @Valid @RequestBody UserStatusUpdateRequestDto request
    ) {
        UserStatusResponseDto updatedUserStatus =
                userApplicationService.updateUserStatusByUserId(userId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedUserStatus);
    }
}
