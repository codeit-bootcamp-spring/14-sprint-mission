package com.sprint.mission.discodeit.application.binarycontent.provided.query;

import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentDownloader {

  ResponseEntity<?> download(UUID binaryContentId);
}
