package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.application.binarycontent.BinaryContentApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
//바이너리 파일 다운로드
//[ ] 바이너리 파일을 1개 또는 여러 개 조회할 수 있다. -- 어떻게 테스트 해야?
public class BinaryContentApiController {

    private final BinaryContentApplicationService binaryContentApplicationService;

    @GetMapping("/api/find")
    public BinaryContentResponseDto findById(
            @Valid @RequestParam("binaryContentId") UUID binaryContentId
    ) {
        return binaryContentApplicationService.findById(binaryContentId);
    }

    // Binary content id의 list를 주면 그 id를 가진 binary content id들을 반환한다
    @GetMapping("/api/findAllByIdIn")
    public List<BinaryContentResponseDto> findAllByIdIn(
            @Valid @RequestParam("binaryContentIds") List<UUID> binaryContentIds
    ) {
        return binaryContentApplicationService.findAllByIdIn(binaryContentIds);
    }

}
