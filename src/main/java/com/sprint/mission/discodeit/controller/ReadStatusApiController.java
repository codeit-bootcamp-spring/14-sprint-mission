package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.service.application.readstatus.ReadStatusApplicationService;
import lombok.RequiredArgsConstructor;
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

    @PatchMapping("/users/{userId}/channels/{channelId}/read")
    public ReadStatusResponseDto markAsRead(
            @PathVariable UUID userId,
            @PathVariable UUID channelId
    ) {
        return readStatusApplicationService.markAsRead(userId, channelId);
    }

    @GetMapping("/users/{userId}")
    public List<ReadStatusResponseDto> retrieveAllByUserId(
            @PathVariable UUID userId
    ) {
        return readStatusApplicationService.findAllByUserId(userId);
    }
}
