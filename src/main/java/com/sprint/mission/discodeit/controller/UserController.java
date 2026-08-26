package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
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

    private BinaryContentCreateRequest profileRequest(
        MultipartFile profile) throws IOException {
        if (profile == null || profile.isEmpty()) {
            return null;
        }
        return new BinaryContentCreateRequest(
            profile.getOriginalFilename(),
            profile.getContentType(),
            profile.getBytes()
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<UserDto> create(
        @RequestPart("userCreateRequest") @Valid UserCreateRequest request,
        @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        log.info("create 정상 작동. user이메일:{}", request.email());

        BinaryContentCreateRequest profileRequest = profileRequest(profile);
        UserDto created = userService.create(request, profileRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> update(
        @PathVariable UUID userId,
        @RequestPart("userUpdateRequest") @Valid UserUpdateRequest request,
        @RequestPart(value = "profile", required = false)
        MultipartFile profile) throws IOException {

        log.info("update 정상 작동. 수정할 userId:{}", userId);
        BinaryContentCreateRequest profileRequest = profileRequest(profile);
        UserDto updated = userService.update(userId, request, profileRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping( "/{userId}")
    public void delete(@PathVariable UUID userId) {
        log.info("delete 정상 작동. 삭제할 userId:{}", userId);
        userService.delete(userId);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        log.info("findAll 정상 작동.");
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PatchMapping ( "/{userId}/userStatus")
    public ResponseEntity<UserStatusDto> userStatusUpdate(
        @PathVariable UUID userId,
        @Valid @RequestBody UserStatusUpdateRequest request) {
        log.info("userStatusUpdate 정상 작동.");

        UserStatusDto userStatus = userStatusService.updateByUserId(userId, request);
        return ResponseEntity.ok(userStatus);
    }

}
