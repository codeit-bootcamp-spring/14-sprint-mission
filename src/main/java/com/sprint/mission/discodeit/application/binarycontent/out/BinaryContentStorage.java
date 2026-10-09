package com.sprint.mission.discodeit.application.binarycontent.out;

import com.sprint.mission.discodeit.adapter.in.controller.dto.binaryContent.BinaryContentResponseDto;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {
    UUID put(UUID id, byte[] bytes);

    InputStream get(UUID id);

    ResponseEntity<?> download(BinaryContentResponseDto binaryContentResponse);
}
