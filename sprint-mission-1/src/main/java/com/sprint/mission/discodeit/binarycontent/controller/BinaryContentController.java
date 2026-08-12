package com.sprint.mission.discodeit.binarycontent.controller;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.binarycontent.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.GET, value = "/api/binaryContent/{id}")
    public BinaryContentResponseDto findById(
        @PathVariable UUID id) {
        return binaryContentService.findBinaryContent(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/binaryContent")
    public List<BinaryContentResponseDto> findAll() {
        return binaryContentService.findAll();
    }
}
