package com.sprint.mission.discodeit.readStatus.controller;

import com.sprint.mission.discodeit.readStatus.dto.*;
import com.sprint.mission.discodeit.readStatus.application.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/readStatuses")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping
    public ReadStatusDto create(@RequestBody ReadStatusCreateRequestDto request){
        log.debug("읽음 상태 생성 요청 - userId : {}, channelId : {}", request.userId(), request.channelId());
        return readStatusService.create(request);
    }

    @PatchMapping(value = "/{readStatusId}")
    public ReadStatusDto update(@PathVariable UUID readStatusId,
                                              @RequestBody ReadStatusUpdateRequestDto request){
        log.debug("읽음 상태 수정 요청 - readStatusId : {}", readStatusId);
        return readStatusService.update(readStatusId, request);
    }

    @GetMapping
    public List<ReadStatusDto> findAllByUserId(@RequestParam UUID userId){
        log.debug("사용자별 읽음 상태 목록 조회 요청 - userId : {}", userId);
        return readStatusService.findAllByUserId(userId);
    }

}
