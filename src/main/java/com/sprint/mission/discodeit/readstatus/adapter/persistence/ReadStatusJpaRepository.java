package com.sprint.mission.discodeit.readstatus.adapter.persistence;

import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusJpaRepository extends JpaRepository<ReadStatus, UUID> {

  List<ReadStatus> findAllByUser_Id(UUID userId);

  List<ReadStatus> findAllByChannel_Id(UUID channelId);
}
