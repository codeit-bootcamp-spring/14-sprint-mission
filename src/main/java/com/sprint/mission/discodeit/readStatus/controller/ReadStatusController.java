package com.sprint.mission.discodeit.readStatus.controller;

import com.sprint.mission.discodeit.readStatus.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.readStatus.dto.ReadStatusUpdateResponseDto;
import com.sprint.mission.discodeit.readStatus.application.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/readStatuses")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping
    public ReadStatusResponseDto create(@RequestBody ReadStatusCreateRequestDto request){
        return readStatusService.create(request);
    }

    @PatchMapping(value = "/{readStatusId}")
    public ReadStatusUpdateResponseDto update(@PathVariable UUID readStatusId,
                                              @RequestBody ReadStatusUpdateRequestDto request){
        return readStatusService.update(readStatusId, request);
    }

    @GetMapping
    public List<ReadStatusResponseDto> findAllByUserId(@RequestParam UUID userId){
        return readStatusService.findAllByUserId(userId);
    }

}
