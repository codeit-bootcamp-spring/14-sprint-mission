package com.sprint.mission.discodeit.binaryContent.controller;

import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.binaryContent.application.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/binaryContents")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @GetMapping
    public List<BinaryContentResponseDto> findAllByIds(@RequestParam List<UUID> binaryContentIds ){
        return binaryContentService.findAllByIdIn(binaryContentIds );
    }

    @GetMapping(value = "/{binaryContentId}")
    public BinaryContentResponseDto findById(@PathVariable UUID binaryContentId){
        return binaryContentService.find(binaryContentId);
    }

}
