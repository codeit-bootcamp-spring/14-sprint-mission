package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/binaryContents")
public class BinaryContentApiController {
    private final BasicBinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.GET)
    public List<BinaryContentResponseDto> getBinaryContents(@Valid @RequestBody BinaryContentRequestDto request) {
        return binaryContentService.getAllBinaryContents(request.binaryContentIds());
    }

    // 심화 요구사항
    @RequestMapping(method = RequestMethod.GET, value = "/find/{binaryContentId}")
    public ResponseEntity<BinaryContent> getBinaryContent(@PathVariable UUID binaryContentId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(binaryContentService.getBinaryContent(binaryContentId));
    }
}
