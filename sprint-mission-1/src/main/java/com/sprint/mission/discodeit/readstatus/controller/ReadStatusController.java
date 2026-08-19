package com.sprint.mission.discodeit.readstatus.controller;

import com.sprint.mission.discodeit.readstatus.dto.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.readstatus.dto.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.readstatus.service.ReadStatusService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/readStatuses")
    public ResponseEntity<List<ReadStatusResponseDto>> findAll(
        @RequestParam UUID userId
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(readStatusService.findAllByUserId(userId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/api/readStatuses")
    public ResponseEntity<ReadStatusResponseDto> create(
        @Valid @RequestBody ReadStatusCreateRequestDto readStatusCreateRequestDto) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(readStatusService.readStatusCreate(readStatusCreateRequestDto));
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/api/readStatuses/{readStatusId}")
    public ResponseEntity<ReadStatusResponseDto> update(
        @PathVariable UUID readStatusId,
        @Valid @RequestBody ReadStatusUpdateRequestDto readStatusUpdateRequestDto) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(readStatusService.readStatusUpdate(readStatusId, readStatusUpdateRequestDto));
    }
}
