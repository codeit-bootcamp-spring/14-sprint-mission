package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readstatusdto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatusdto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readstatusdto.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatus")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST, value = "")
    public ReadStatusResponseDto create(@RequestBody ReadStatusCreateRequestDto dto) {
        return readStatusService.createReadStatus(dto);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public ReadStatusResponseDto modify(@PathVariable UUID id,
                                        @RequestBody ReadStatusUpdateRequestDto dto) {
        return readStatusService.updateReadStatus(id,dto);
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<ReadStatusResponseDto> read(@RequestParam UUID userId) {
        return readStatusService.findAllByUserId(userId);
    }
}
