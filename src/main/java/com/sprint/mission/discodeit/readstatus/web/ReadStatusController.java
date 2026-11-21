package com.sprint.mission.discodeit.readstatus.web;

import com.sprint.mission.discodeit.readstatus.domain.entity.ReadStatus;
import com.sprint.mission.discodeit.readstatus.application.ReadStatusApplicationService;
import com.sprint.mission.discodeit.readstatus.web.dto.req.ReadStatusCreateRequestDTO;
import com.sprint.mission.discodeit.readstatus.web.dto.req.ReadStatusUpdateRequestDTO;
import com.sprint.mission.discodeit.readstatus.web.dto.res.ReadStatusResponseDTO;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
public class ReadStatusController {
    private final ReadStatusApplicationService readStatusApplicationService;

    @GetMapping
    public ResponseEntity<List<ReadStatusResponseDTO>> findReadStatusesByUserId(@RequestParam UUID userId){
        List<ReadStatusResponseDTO> response = readStatusApplicationService.findReadStatusByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @PostMapping
    public ResponseEntity<ReadStatusResponseDTO> createReadStatus(
        @Valid @RequestBody ReadStatusCreateRequestDTO readStatusCreateRequestDTO
    ){
        ReadStatusResponseDTO response = readStatusApplicationService.createReadStatus(readStatusCreateRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusResponseDTO> updateReadStatus(
        @PathVariable UUID readStatusId,
        @RequestBody ReadStatusUpdateRequestDTO readStatusUpdateRequestDTO)
    {
        ReadStatusResponseDTO response = readStatusApplicationService
            .updateReadStatusReadTime(readStatusId, readStatusUpdateRequestDTO.newLastReadAt());

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }
}
