package com.sprint.mission.discodeit.readstatus.adapter.persistence;

import com.sprint.mission.discodeit.readstatus.application.required.ReadStatusRepository;
import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReadStatusRepositoryAdapter implements ReadStatusRepository {

  private final ReadStatusJpaRepository jpaRepository;

  @Override
  public List<ReadStatus> findAllByUser_Id(UUID userId) {
    return jpaRepository.findAllByUser_Id(userId);
  }

  @Override
  public List<ReadStatus> findAllByChannel_Id(UUID channelId) {
    return jpaRepository.findAllByChannel_Id(channelId);
  }

  @Override
  public Optional<ReadStatus> findById(UUID readStatusID) {
    return jpaRepository.findById(readStatusID);
  }

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    return jpaRepository.save(readStatus);
  }

  @Override
  public List<ReadStatus> saveAll(List<ReadStatus> readStatuses) {
    return jpaRepository.saveAll(readStatuses);
  }

  @Override
  public void deleteAll(List<ReadStatus> readStatuses) {
    jpaRepository.deleteAll(readStatuses);
  }

  @Override
  public void delete(ReadStatus readStatus) {
    jpaRepository.delete(readStatus);
  }
}
