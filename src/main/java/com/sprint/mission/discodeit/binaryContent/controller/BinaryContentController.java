package com.sprint.mission.discodeit.binaryContent.controller;

import com.sprint.mission.discodeit.binaryContent.application.BinaryContentService;
import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binaryContent.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/binaryContents")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @GetMapping
    public List<BinaryContentDto> findAllByIds(@RequestParam List<UUID> binaryContentIds) {
        log.debug("아이디들로 찾기 요청 - Ids : {}", binaryContentIds);
        return binaryContentService.findAllByIdIn(binaryContentIds);
    }

    @GetMapping(value = "/{binaryContentId}")
    public BinaryContentDto findById(@PathVariable UUID binaryContentId) {
        log.debug("아이디로 찾기 요청 - Id : {}", binaryContentId);
        return binaryContentService.find(binaryContentId);
    }

    @GetMapping(value = "/{binaryContentId}/download")
    public ResponseEntity<?> downloadById(@PathVariable UUID binaryContentId) {
        log.debug("아이디로 찾기 요청(다운로드) - Id : {}", binaryContentId);
        BinaryContentDto dto = binaryContentService.find(binaryContentId);
        return binaryContentStorage.download(dto);
    }

}
