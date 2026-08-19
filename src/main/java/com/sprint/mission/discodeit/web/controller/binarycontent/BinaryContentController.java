package com.sprint.mission.discodeit.web.controller.binarycontent;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.service.binarycontent.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binary-content")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> findBinaryContent(@PathVariable UUID id){
        //return ResponseEntity.ok(bbinaryContentService.findStoreFile(id));

        BinaryContent storeFile = binaryContentService.findStoreFile(id);
        String pathUrl = storeFile.getPathUrl();
        Resource resource = new FileSystemResource(pathUrl);

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(storeFile.getFileType()))
            .body(resource);
    }

    @GetMapping
    public ResponseEntity<List<BinaryContent>> findAllBinaryContent(){
        return ResponseEntity.ok(binaryContentService.findAllStoreFile());
    }
}
