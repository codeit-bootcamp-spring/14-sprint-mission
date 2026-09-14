package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.service.basic.ReadStatusService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

    public final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReadStatusResponseDto> create(
        @RequestBody ReadStatusCreateRequestDto request
    ) {
        ReadStatusResponseDto response = readStatusService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/{readStatusId}")
    public ResponseEntity<ReadStatusResponseDto> update(
        @PathVariable UUID readStatusId,
        @RequestBody ReadStatusUpdateRequestDto request
    ) {
        ReadStatusResponseDto response = readStatusService.update(readStatusId, request);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatusResponseDto>> findAllByUserId(
        @RequestParam UUID userId
    ) {
        List<ReadStatusResponseDto> response = readStatusService.findAllByUserId(userId);
        return ResponseEntity.ok(response);
    }
}
