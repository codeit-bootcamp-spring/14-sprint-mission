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
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/binary-contents/{id}")
    public ResponseEntity<BinaryContentResponseDto> findById(
        @PathVariable UUID id) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(binaryContentService.findBinaryContent(id));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/binary-contents")
    public ResponseEntity<List<BinaryContentResponseDto>> findAll() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(binaryContentService.findAll());
    }
}
