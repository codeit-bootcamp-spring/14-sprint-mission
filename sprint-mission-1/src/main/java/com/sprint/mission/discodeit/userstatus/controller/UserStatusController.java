package com.sprint.mission.discodeit.userstatus.controller;

import com.sprint.mission.discodeit.userstatus.dto.UserStatusResponseDto;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.userstatus.service.UserStatusService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserStatusController {

    private final UserStatusService userStatusService;

    // 사용자 상태 업데이트
    @RequestMapping(method = RequestMethod.PATCH, value = "/api/users/{userId}/userStatus")
    public ResponseEntity<UserStatusResponseDto> userStatusUpdate(
        @PathVariable UUID userId,
        @Valid @RequestBody UserStatusUpdateRequestDto userStatusUpdateRequestDto) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userStatusService.userStatusUpdateByUserId(userId, userStatusUpdateRequestDto));
    }
}
