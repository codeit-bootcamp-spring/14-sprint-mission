package com.sprint.mission.discodeit.binaryContent.repository;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {
    List<BinaryContent> findAllByIdIn(List<UUID> ids);

}
