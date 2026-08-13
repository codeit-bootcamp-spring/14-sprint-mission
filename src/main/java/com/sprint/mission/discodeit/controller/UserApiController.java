package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserAndBinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.UserAndBinaryContentUpsertRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpsertRequestDto;
import com.sprint.mission.discodeit.service.application.user.UserApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/api/users")
    public UserResponseDto create(
            @RequestBody UserAndBinaryContentUpsertRequestDto upsertRequest
    ) {
        return userApplicationService.create(
                upsertRequest.getUser(),    // UserCreateRequestDto
                upsertRequest.getProfile()  // BinaryContentCreateRequestDto
        );
    }

    @PutMapping("/api/users/{id}")
    public UserResponseDto update(
            @PathVariable UUID id,
            @RequestBody UserAndBinaryContentUpsertRequestDto upsertRequest
    ) {
        return userApplicationService.update(
                id,
                upsertRequest.getUser(),    // UserCreateRequestDto
                upsertRequest.getProfile()  // BinaryContentCreateRequestDto
        );
    }

    @DeleteMapping("/api/users/{id}")
    public void delete(@PathVariable UUID id) {
        userApplicationService.delete(id);
    }

    @GetMapping("/api/users")
    public List<UserResponseDto> retrieveAll() {
        return userApplicationService.findAll();
    }

    @PatchMapping("/api/users/{id}/active")
    public UserResponseDto activateUser(@PathVariable UUID id) {
        return userApplicationService.activateUser(id);
    }
}
