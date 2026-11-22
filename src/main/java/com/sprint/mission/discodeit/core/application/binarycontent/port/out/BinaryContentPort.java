package com.sprint.mission.discodeit.core.application.binarycontent.port.out;

import com.sprint.mission.discodeit.core.common.persistence.CrudRepository;
import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContentException;
import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContentExceptionType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BinaryContentPort extends CrudRepository<BinaryContent, UUID> {
    List<BinaryContent> findAllById(List<UUID> ids);
}
