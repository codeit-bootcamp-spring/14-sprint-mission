package com.sprint.mission.discodeit.readstatus.application.required;

import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {

  List<ReadStatus> findAllByUser_Id(UUID userId);

  List<ReadStatus> findAllByChannel_Id(UUID channelId);


  Optional<ReadStatus> findById(UUID readStatusID);

  ReadStatus save(ReadStatus readStatus);

  List<ReadStatus> saveAll(List<ReadStatus> readStatuses);

  void deleteAll(List<ReadStatus> readStatuses);

  void delete(ReadStatus readStatus);
}
