package com.sprint.mission.discodeit.adapter.in.web.controller.binaryContent;

import com.sprint.mission.discodeit.adapter.in.web.controller.binaryContent.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.core.application.binarycontent.port.out.dto.response.DownloadContent;
import com.sprint.mission.discodeit.core.application.binarycontent.port.in.BinaryContentUseCase;
import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.response.BinaryContentDownloadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/binaryContents")
public class BinaryContentApiController {
    private final BinaryContentUseCase useCase;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<BinaryContentResponseDto> getBinaryContents(@RequestParam List<UUID> binaryContentIds) {
        return useCase.getAllBinaryContentInfos(binaryContentIds).stream()
                .map(BinaryContentResponseDto::from)
                .toList();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{binaryContentId}")
    public BinaryContentResponseDto getBinaryContent(@PathVariable UUID binaryContentId) {
        return BinaryContentResponseDto.from(useCase.getBinaryContentInfo(binaryContentId));
    }

    @GetMapping(value = "/{binaryContentId}/download")
    public ResponseEntity<?> downloadBinaryContent(@PathVariable UUID binaryContentId) {
        BinaryContentDownloadResult result = useCase.downloadBinaryContent(binaryContentId);
        MediaType contentType = MediaType.parseMediaType(result.contentType());
        return switch (result.content()) {
            case DownloadContent.Redirect(var location) -> ResponseEntity.status(HttpStatus.FOUND)
                    .location(location)
                    .contentType(contentType)
                    .build();
            case DownloadContent.Stream(var inputStream) -> ResponseEntity.ok()
                    .contentType(contentType)
                    .body(new InputStreamResource(inputStream));
        };
    }
}
