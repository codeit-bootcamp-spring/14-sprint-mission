package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(@Valid BinaryContentCreateDto dto);

    BinaryContent getBinaryContent(UUID id);

    List<BinaryContent> getAllBinaryContents(List<UUID> ids);

    void delete(UUID id);
}
