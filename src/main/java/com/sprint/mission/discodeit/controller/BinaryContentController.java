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

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public BinaryContentResponseDto read(@PathVariable UUID id) {
        return binaryContentService.readBinaryContent(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<BinaryContentResponseDto> readAll(@RequestParam List<UUID> ids) {
        return binaryContentService.findAllByIdIn(ids);
    }

}
