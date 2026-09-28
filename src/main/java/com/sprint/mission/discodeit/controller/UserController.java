package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> create(
        @RequestPart("userCreateRequest") @Valid UserCreateRequest request,
        @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        log.info("create 요청. email:{}", request.email());
        UserDto created = userService.create(request, toProfileRequest(profile));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> update(
        @PathVariable UUID userId,
        @RequestPart("userUpdateRequest") @Valid UserUpdateRequest request,
        @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        log.info("update 요청. userId:{}", userId);
        return ResponseEntity.ok(userService.update(userId, request, toProfileRequest(profile)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        log.info("delete 요청. userId:{}", userId);
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        log.info("findAll 요청");
        return ResponseEntity.ok(userService.findAll());
    }

    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatusDto> updateUserStatus(
        @PathVariable UUID userId,
        @Valid @RequestBody UserStatusUpdateRequest request) {
        log.info("updateUserStatus 요청. userId:{}", userId);
        return ResponseEntity.ok(userStatusService.updateByUserId(userId, request));
    }

    private BinaryContentCreateRequest toProfileRequest(MultipartFile profile) {
        if (profile == null || profile.isEmpty()) {
            return null;
        }
        try {
            return new BinaryContentCreateRequest(
                profile.getOriginalFilename(), profile.getContentType(), profile.getBytes());
        } catch (IOException e) {
            throw new DiscodeitException(ErrorCode.FILE_STORAGE_ERROR,
                "프로필 이미지를 읽을 수 없습니다: " + profile.getOriginalFilename());
        }
    }
}
