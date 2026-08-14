package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpsertRequestDto;
import com.sprint.mission.discodeit.service.application.user.UserApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class UserApiController {

    private final UserApplicationService userApplicationService;

    // ### Multipart로
    @PostMapping("/api/users")
    public UserResponseDto create(
            @Valid @RequestPart(value = "user") UserUpsertRequestDto userCreateRequest,
            @Valid @RequestPart(value = "profile", required = false) MultipartFile profileImage
    ) {
        return userApplicationService.create(
                userCreateRequest,
                profileImage
        );
    }

    // ### Multipart로
    @PutMapping("/api/users/{id}")
    public UserResponseDto update(
            @Valid @PathVariable UUID id,
            @Valid @RequestPart UserUpsertRequestDto userUpdateRequest,
            @Valid @RequestPart(required = false) MultipartFile profileImage
    ) {
        return userApplicationService.update(
                id,
                userUpdateRequest,
                profileImage
        );
    }

    @DeleteMapping("/api/users/{id}")
    public void delete(@Valid @PathVariable UUID id) {
        userApplicationService.delete(id);
    }

    @GetMapping("/api/users")
    public List<UserResponseDto> retrieveAll() {
        return userApplicationService.findAll();
    }

    @PatchMapping("/api/users/{id}/active")
    public UserResponseDto activateUser(@Valid @PathVariable UUID id) {
        return userApplicationService.activateUser(id);
    }
}
