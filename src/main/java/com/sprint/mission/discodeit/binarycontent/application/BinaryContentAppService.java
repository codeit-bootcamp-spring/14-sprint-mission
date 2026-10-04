package com.sprint.mission.discodeit.binarycontent.application;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binarycontent.application.provided.query.BinaryContentDownloader;
import com.sprint.mission.discodeit.binarycontent.application.provided.query.BinaryContentFinder;
import com.sprint.mission.discodeit.binarycontent.application.required.BinaryContentStorage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BinaryContentAppService implements BinaryContentDownloader {

  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentFinder binaryContentFinder;

  @Override
  public ResponseEntity<?> download(UUID binaryContentId) {
    BinaryContentDto binaryContentDto = binaryContentFinder.getById(binaryContentId);

    return binaryContentStorage.download(binaryContentDto);
  }
}
