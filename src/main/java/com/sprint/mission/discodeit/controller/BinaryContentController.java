package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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
    private final BinaryContentStorage binaryContentStorage;

    @GetMapping
    public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
        @RequestParam List<UUID> binaryContentIds) {
        log.info("findAllByIdIn 요청. ids:{}", binaryContentIds);
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(binaryContentIds));
    }

    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentDto> find(@PathVariable UUID binaryContentId) {
        log.info("find 요청. id:{}", binaryContentId);
        return ResponseEntity.ok(binaryContentService.find(binaryContentId));
    }

    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {
        log.info("download 요청. id:{}", binaryContentId);
        BinaryContentDto binaryContent = binaryContentService.find(binaryContentId);
        return binaryContentStorage.download(binaryContent);
    }
}
