package com.sprint.mission.discodeit.binarycontent.controller;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.binarycontent.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/binaryContents/{binaryContentId}")
    public ResponseEntity<BinaryContentResponseDto> findById(
        @PathVariable UUID binaryContentId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(binaryContentService.findBinaryContent(binaryContentId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/binaryContents")
    public ResponseEntity<List<BinaryContentResponseDto>> findAll(
        @RequestParam List<UUID> binaryContentIds
    ) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(binaryContentService.findAll());
    }
}
