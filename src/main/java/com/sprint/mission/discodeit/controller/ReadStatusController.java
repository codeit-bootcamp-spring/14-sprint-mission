package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

   @PostMapping
    public ResponseEntity<ReadStatusDto> create(
        @Valid @RequestBody ReadStatusCreateRequest request) {
        log.info("create 정상 작동. channelId:{}", request.channelId());

        ReadStatusDto created = readStatusService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusDto> update(
        @PathVariable UUID readStatusId,
        @RequestBody ReadStatusUpdateRequest request) {

        log.info("update 정상 작동.");
        ReadStatusDto updated = readStatusService.update(readStatusId, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public ResponseEntity<List<ReadStatusDto>> findAllByUserId(
        @RequestParam UUID userId) {
        log.info("findAllByUserId 정상 작동. Id:{}", userId);

        List<ReadStatusDto> findAllUser = readStatusService.findAllByUserId(userId);

        return ResponseEntity.ok(findAllUser);
    }
}
