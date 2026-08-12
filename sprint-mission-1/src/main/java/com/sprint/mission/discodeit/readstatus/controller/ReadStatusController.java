package com.sprint.mission.discodeit.readstatus.controller;

import com.sprint.mission.discodeit.readstatus.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.readstatus.service.ReadStatusService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST, value = "/api/readStatus")
    public ReadStatusResponseDto create(
        @Valid @RequestBody ReadStatusCreateRequestDto readStatusCreateRequestDto) {
        return readStatusService.readStatusCreate(readStatusCreateRequestDto);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/api/readStatus/{id}")
    public void update(
        @PathVariable UUID id,
        @Valid @RequestBody ReadStatusUpdateRequestDto readStatusUpdateRequestDto) {
        readStatusService.readStatusUpdate(id, readStatusUpdateRequestDto);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/readStatus/{id}/find")
    public ReadStatusResponseDto find(
        @PathVariable UUID id) {
        return readStatusService.findReadStatus(id);
    }
}
