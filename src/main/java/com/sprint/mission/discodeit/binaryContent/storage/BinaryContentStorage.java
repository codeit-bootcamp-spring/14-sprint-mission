package com.sprint.mission.discodeit.binaryContent.storage;

import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentDto;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {
    UUID put(UUID binaryContentId, byte[] bytes);
    InputStream get(UUID binaryContentId);
    ResponseEntity<?> download(BinaryContentDto dto);
    
}
