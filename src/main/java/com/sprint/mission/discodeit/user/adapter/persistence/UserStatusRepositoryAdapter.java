package com.sprint.mission.discodeit.user.adapter.persistence;

import com.sprint.mission.discodeit.user.application.required.UserStatusRepository;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserStatusRepositoryAdapter implements UserStatusRepository {

  private final UserStatusJpaRepository statusJpaRepository;

  @Override
  public Optional<UserStatus> findByUser_Id(UUID userId) {
    return statusJpaRepository.findByUser_Id(userId);
  }
}
