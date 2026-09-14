package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDto;
import com.sprint.mission.discodeit.service.IService.UserService;
import com.sprint.mission.discodeit.service.basic.UserStatusService;
import com.sprint.mission.discodeit.util.BinaryContentMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;
    private final BinaryContentMapper binaryContentMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> create(
        @RequestPart("userCreateRequest") UserCreateRequestDto request,
        @RequestPart(value = "profile", required = false) MultipartFile profile) {
        BinaryContentCreateRequestDto profileRequest =
            binaryContentMapper.toBinaryContentCreateRequestDto(profile);
        UserResponseDto response = userService.create(request, profileRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> update(
        @PathVariable UUID userId,
        @RequestPart("userUpdateRequest") UserUpdateRequestDto request,
        @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        BinaryContentCreateRequestDto profileRequest =
            binaryContentMapper.toBinaryContentCreateRequestDto(profile);
        UserResponseDto response = userService.update(userId, request, profileRequest);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<UserResponseDto> response = userService.readAll();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/{userId}/userStatus")
    public ResponseEntity<UserStatusResponseDto> updateUserStatus(
        @PathVariable UUID userId,
        @RequestBody UserStatusUpdateDto request
    ) {
        UserStatusResponseDto response = userStatusService.updateByUserId(userId, request);
        return ResponseEntity.ok(response);
    }


}
