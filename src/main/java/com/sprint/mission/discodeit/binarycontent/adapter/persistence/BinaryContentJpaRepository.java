package com.sprint.mission.discodeit.binarycontent.adapter.persistence;

import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinaryContentJpaRepository extends JpaRepository<BinaryContent, UUID> {

}
