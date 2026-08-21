package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.application.BinaryContentApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/binaryContents")
public class BinaryContentApiController {
    private final BinaryContentApplication binaryContentApplication;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET)
    public List<BinaryContentResponseDto> getBinaryContents(@RequestParam List<UUID> binaryContentIds) {
        return binaryContentApplication.getAllBinaryContents(binaryContentIds);
    }

    // 심화 요구사항
    @RequestMapping(method = RequestMethod.GET, value = "/find/{binaryContentId}")
    public ResponseEntity<BinaryContent> getBinaryContent(@PathVariable UUID binaryContentId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(binaryContentApplication.getBinaryContent(binaryContentId));
    }
}
