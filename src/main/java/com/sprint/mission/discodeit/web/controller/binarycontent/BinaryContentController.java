package com.sprint.mission.discodeit.web.controller.binarycontent;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.service.binarycontent.BinaryContentService;
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.web.controller.dto.res.BinaryContentResponseDTO;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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
    private final BinaryContentService binaryContentService;

    //todo
    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentResponseDTO> findBinaryContent(@PathVariable UUID binaryContentId) {
        //return ResponseEntity.ok(bbinaryContentService.findStoreFile(id));

        BinaryContent storeFile = binaryContentService.findStoreFile(binaryContentId);
//        String pathUrl = storeFile.getPathUrl();
//        Resource resource = new FileSystemResource(pathUrl);

        // 응답규격에 맞게 일단 수정
        byte bytes[] = convertBinaryFile(storeFile);
        BinaryContentResponseDTO response = BinaryContentResponseDTO.of(storeFile, bytes.length, bytes);

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<BinaryContentResponseDTO>> findBinaryContents(@RequestParam List<UUID> binaryContentIds){
        List<BinaryContent> binaryContentList = binaryContentService.findAllStoreFileByIdIn(
            binaryContentIds);

        List<BinaryContentResponseDTO> response = binaryContentList.stream()
            .map(binaryContent -> {
                byte bytes[] = convertBinaryFile(binaryContent);
                return BinaryContentResponseDTO.of(binaryContent, bytes.length, bytes);
            })
            .toList();

        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    private byte[] convertBinaryFile(BinaryContent binaryContent){
        try{
            return Files.readAllBytes(Paths.get(binaryContent.getPathUrl()));
        }catch (IOException e){
            log.error("convert error",e);
        }

        throw new RuntimeException("파일 변환 실패");
    }
}
