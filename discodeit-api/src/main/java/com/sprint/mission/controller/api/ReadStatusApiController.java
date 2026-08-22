package com.sprint.mission.controller.api;

import com.sprint.mission.application.readstatus.ReadStatusApplicationService;
import com.sprint.mission.controller.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.controller.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.controller.dto.readstatus.ReadStatusUpdateRequest;
import jakarta.validation.Valid;
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
@RequestMapping("/api/readStatuses")
@Validated
// 메시지 수신 정보 관리
// [X] 채널 생성 시 메시지 수신 정보를 함께 생성할 수 있다.
// [X] 특정 사용자의 특정 채널 메시지 수신 정보를 수정할 수 있다.
// [X] 특정 사용자의 메시지 수신 정보를 조회할 수 있다.
public class ReadStatusApiController {

    private final ReadStatusApplicationService readStatusApplicationService;

    @PostMapping
    public ResponseEntity<ReadStatusResponseDto> create(
            @Valid @RequestBody ReadStatusCreateRequest request
    ) {
        ReadStatusResponseDto createdReadStatus = readStatusApplicationService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdReadStatus);
    }

    @GetMapping
    public ResponseEntity<List<ReadStatusResponseDto>> findAllByUserId(
            @NotNull @RequestParam UUID userId
    ) {
        List<ReadStatusResponseDto> readStatusListByUserId = readStatusApplicationService.findAllByUserId(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(readStatusListByUserId);
    }

    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusResponseDto> update(
            @NotNull @PathVariable UUID readStatusId,
            @Valid @RequestBody ReadStatusUpdateRequest request
    ) {
        ReadStatusResponseDto updatedReadStatus = readStatusApplicationService.update(readStatusId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedReadStatus);
    }
}
