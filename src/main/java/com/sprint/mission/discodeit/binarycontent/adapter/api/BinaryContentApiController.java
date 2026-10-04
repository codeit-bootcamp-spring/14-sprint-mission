package com.sprint.mission.discodeit.binarycontent.adapter.api;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binarycontent.application.provided.query.BinaryContentDownloader;
import com.sprint.mission.discodeit.binarycontent.application.provided.query.BinaryContentFinder;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentApiController {

  private final BinaryContentDownloader binaryContentDownloader;
  private final BinaryContentFinder binaryContentFinder;

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(
      @PathVariable UUID binaryContentId) {
    return binaryContentDownloader.download(binaryContentId);
  }

  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> find(
      @PathVariable UUID binaryContentId) {
    BinaryContentDto binaryContentDto = binaryContentFinder.getById(binaryContentId);
    return ResponseEntity.ok(binaryContentDto);
  }

  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @RequestParam(name = "binaryContentIds") List<UUID> binaryContentIds) {
    return ResponseEntity.ok(
        binaryContentFinder.getAllByIds(binaryContentIds));
  }
}
