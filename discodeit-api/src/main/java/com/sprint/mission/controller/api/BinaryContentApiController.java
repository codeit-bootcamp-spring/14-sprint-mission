package com.sprint.mission.controller.api;

import com.sprint.mission.application.binarycontent.BinaryContentApplicationService;
import com.sprint.mission.controller.dto.binarycontent.BinaryContentResponseDto;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentApiController {

    private final BinaryContentApplicationService binaryContentApplicationService;

    @GetMapping("/{binaryContentId}")
    public BinaryContentResponseDto findById(
            @NotNull @PathVariable UUID binaryContentId
    ) {
        return binaryContentApplicationService.findById(binaryContentId);
    }

    // Binary content id의 list를 주면 그 id를 가진 binary content id들을 반환한다
    @GetMapping
    public List<BinaryContentResponseDto> findAllByIdIn(
            @RequestParam List<UUID> binaryContentIds
    ) {
        return binaryContentApplicationService.findAllByIdIn(binaryContentIds);
    }
}
