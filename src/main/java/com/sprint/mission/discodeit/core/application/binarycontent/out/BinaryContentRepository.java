package com.sprint.mission.discodeit.core.application.binarycontent.out;


import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {
}
