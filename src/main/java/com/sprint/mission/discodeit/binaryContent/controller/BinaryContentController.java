package com.sprint.mission.discodeit.binaryContent.controller;

import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentDownloadResponse;
import com.sprint.mission.discodeit.binaryContent.application.BinaryContentService;
import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binaryContent.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/binaryContents")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @GetMapping
    public List<BinaryContentDto> findAllByIds(@RequestParam List<UUID> binaryContentIds ){
        return binaryContentService.findAllByIdIn(binaryContentIds );
    }

    @GetMapping(value = "/{binaryContentId}")
    public BinaryContentDto findById(@PathVariable UUID binaryContentId){
        return binaryContentService.find(binaryContentId);
    }

    @GetMapping(value = "/{binaryContentId}/download")
    public ResponseEntity<?> downloadById(@PathVariable UUID binaryContentId){
        BinaryContentDto dto = binaryContentService.find(binaryContentId);
        return binaryContentStorage.download(dto);
    }

}
