package com.sprint.mission.controller.api;

import com.sprint.mission.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.application.binarycontent.BinaryContentApplicationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binary-content")
//바이너리 파일 다운로드
//[X] 바이너리 파일을 1개 또는 여러 개 조회할 수 있다.
public class BinaryContentApiController {

    private final BinaryContentApplicationService binaryContentApplicationService;

    @GetMapping("/{binaryContentId}/find")
    public BinaryContentResponseDto findById(
            @NotNull @PathVariable UUID binaryContentId
    ) {
        return binaryContentApplicationService.findById(binaryContentId);
    }

    // Binary content id의 list를 주면 그 id를 가진 binary content id들을 반환한다
    @GetMapping("{binaryContentIds}/findAllByIdIn")
    public List<BinaryContentResponseDto> findAllByIdIn(
            @Valid @PathVariable List<UUID> binaryContentIds
    ) {
        return binaryContentApplicationService.findAllByIdIn(binaryContentIds);
    }

}
