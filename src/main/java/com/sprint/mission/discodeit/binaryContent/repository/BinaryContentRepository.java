package com.sprint.mission.discodeit.binaryContent.repository;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    void save(BinaryContent binaryContent);
    Optional<BinaryContent> findById(UUID id);
    List<BinaryContent> findAll();
    List<BinaryContent> findAllByIdIn(List<UUID> ids);
    void deleteById(UUID id);
}
