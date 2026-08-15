package com.sprint.mission.controller.api;

import com.sprint.mission.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.application.readstatus.ReadStatusApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

public class ReadStatusApiController {

    private final ReadStatusApplicationService readStatusApplicationService;

    // public ReadStatusResponseDto create(...) 는 구현 안 함
    // - User 만들거나, Channel 만들때 생성되는 로직으로 UserApplicationService에서 구현함

    @PatchMapping("/read")
    public ResponseEntity<ReadStatusResponseDto> markAsRead(
            @Valid @RequestParam UUID userId,
            @Valid @RequestParam UUID channelId
    ) {
        ReadStatusResponseDto readStatusMarkedAsRead = readStatusApplicationService.markAsRead(userId, channelId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(readStatusMarkedAsRead);
    }

    @GetMapping
    public ResponseEntity<List<ReadStatusResponseDto>> retrieveAllByUserId(
            @Valid @RequestParam UUID userId
    ) {
        List<ReadStatusResponseDto> readStatusListByUserId = readStatusApplicationService.findAllByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(readStatusListByUserId);
    }
}
