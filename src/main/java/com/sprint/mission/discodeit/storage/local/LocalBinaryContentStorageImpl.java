package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
@Component
@RequiredArgsConstructor
public class LocalBinaryContentStorageImpl implements BinaryContentStorage {


  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    return null;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    return null;
  }

  @Override
  public ResponseEntity<?> download(BinaryContent metaData) {
    return null;
  }
}
