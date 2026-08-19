package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/binary-contents")
public class BinaryContentApiController {
    private final BasicBinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.GET)
    public List<BinaryContentResponseDto> getBinaryContents(@Valid @RequestBody BinaryContentRequestDto request) {
        return binaryContentService.getAllBinaryContents(request.binaryContentIds());
    }
}
