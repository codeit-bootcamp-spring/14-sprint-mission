package com.sprint.mission.discodeit.binarycontent.web;

import com.sprint.mission.discodeit.binarycontent.application.BinaryApplicationService;
import com.sprint.mission.discodeit.binarycontent.application.BinaryContentDownload;
import com.sprint.mission.discodeit.binarycontent.web.dto.res.BinaryContentResponseDTO;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController {
    private final BinaryApplicationService binaryApplicationService;

    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentResponseDTO> findBinaryContent(@PathVariable UUID binaryContentId) {

        BinaryContentResponseDTO response = binaryApplicationService.findStoreFile(binaryContentId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<BinaryContentResponseDTO>> findBinaryContents(@RequestParam List<UUID> binaryContentIds){
        List<BinaryContentResponseDTO> response = binaryApplicationService.findAllStoreFileByIdIn(binaryContentIds);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID binaryContentId){
        BinaryContentDownload download = binaryApplicationService.download(binaryContentId);

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(download.contentType()))
            .contentLength(download.size())
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.attachment()
                    .filename(download.fileName(), StandardCharsets.UTF_8)
                    .build()
                    .toString()
            )
            .body(download.resource());
    }
}
