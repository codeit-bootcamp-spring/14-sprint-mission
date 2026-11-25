package com.sprint.mission.discodeit.binarycontent.application.required;

import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {

  BinaryContent save(BinaryContent binaryContent);

  Optional<BinaryContent> findById(UUID binaryContentId);

  List<BinaryContent> findAllById(List<UUID> binaryContentIds);
}
