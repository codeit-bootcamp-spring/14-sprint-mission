package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {
    void delete(List<UUID> ids);
    List<BinaryContent> findAllById(List<UUID> ids);
    boolean existsById(List<UUID> ids);
}
