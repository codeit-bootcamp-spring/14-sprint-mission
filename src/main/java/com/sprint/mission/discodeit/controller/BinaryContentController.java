package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @GetMapping
    public ResponseEntity<List<BinaryContentDto>> findAll(
        @RequestParam List<UUID> binaryContentIds) {
        log.info("findAll 정상 작동.");
        List<BinaryContentDto> result =
            binaryContentService.findAllByIdIn(binaryContentIds)
                .stream()
                .map(BinaryContentDto::from)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentDto> find(@PathVariable UUID binaryContentId) {
        log.info("find 정상 작동. id:{}", binaryContentId);
        BinaryContentDto dto = binaryContentService.find(binaryContentId)
            .map(BinaryContentDto::from)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.BINARY_CONTENT_NOT_FOUND,
                "BinaryContent를 찾을 수 없습니다. id: " + binaryContentId));
        return ResponseEntity.ok(dto);
    }


}
