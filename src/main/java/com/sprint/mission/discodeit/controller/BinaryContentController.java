package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontentdto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.GET, value = "/{binaryContentId}")
    public BinaryContentResponseDto read(@PathVariable UUID binaryContentId) {
        return binaryContentService.readBinaryContent(binaryContentId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<BinaryContentResponseDto> readAll(@RequestParam List<UUID> binaryContentIds) {
        return binaryContentService.findAllByIdIn(binaryContentIds);
    }
}
