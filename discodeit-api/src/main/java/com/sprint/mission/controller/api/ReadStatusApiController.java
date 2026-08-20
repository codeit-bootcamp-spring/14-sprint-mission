package com.sprint.mission.controller.api;

import com.sprint.mission.controller.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.application.readstatus.ReadStatusApplicationService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/read-status")
// 메시지 수신 정보 관리
// [X] 채널 생성 시 메시지 수신 정보를 함께 생성할 수 있다.
// [X] 특정 사용자의 특정 채널 메시지 수신 정보를 수정할 수 있다.
// [X] 특정 사용자의 메시지 수신 정보를 조회할 수 있다.
@Validated
public class ReadStatusApiController {

    private final ReadStatusApplicationService readStatusApplicationService;

    // public ReadStatusResponseDto create(...) 는 구현 안 함
    // - User 만들거나, Channel 만들때 생성되는 로직으로 UserApplicationService에서 구현함

    @PatchMapping("/{userId}/{channelId}/read")
    public ResponseEntity<ReadStatusResponseDto> markAsRead(
            @NotNull @PathVariable UUID userId,
            @NotNull @PathVariable UUID channelId
    ) {
        ReadStatusResponseDto readStatusMarkedAsRead = readStatusApplicationService.markAsRead(userId, channelId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(readStatusMarkedAsRead);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ReadStatusResponseDto>> retrieveAllByUserId(
            @NotNull @PathVariable UUID userId
    ) {
        List<ReadStatusResponseDto> readStatusListByUserId = readStatusApplicationService.findAllByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(readStatusListByUserId);
    }
}
