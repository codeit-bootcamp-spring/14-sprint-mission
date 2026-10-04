package com.sprint.mission.discodeit.binarycontent.application.required;

import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {

}
