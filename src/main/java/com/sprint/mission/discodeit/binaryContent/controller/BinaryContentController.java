package com.sprint.mission.discodeit.binaryContent.controller;

import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.binaryContent.application.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/binary")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;


    @GetMapping
    public List<BinaryContentResponseDto> create(@RequestParam List<UUID> ids){
        return binaryContentService.findAllByIdIn(ids);
    }

}
