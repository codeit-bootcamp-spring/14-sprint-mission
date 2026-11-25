package com.sprint.mission.discodeit.binarycontent.application.required;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentDto;
import com.sprint.mission.discodeit.common.DeletionEvent;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

  UUID put(UUID id, byte[] bytes);

  InputStream get(UUID id);

  ResponseEntity<?> download(BinaryContentDto binaryContentDto);


  public void handle(DeletionEvent<List<UUID>> deletionEvent);


}
