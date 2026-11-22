package com.sprint.mission.discodeit.adapter.out.persistence.binarycontent;

import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BinaryContentJpaRepository extends JpaRepository<BinaryContent, UUID> {
}
