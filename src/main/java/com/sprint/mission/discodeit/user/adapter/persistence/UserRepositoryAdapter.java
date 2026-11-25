package com.sprint.mission.discodeit.user.adapter.persistence;

import com.sprint.mission.discodeit.user.application.required.UserRepository;
import com.sprint.mission.discodeit.user.domain.Email;
import com.sprint.mission.discodeit.user.domain.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryAdapter implements UserRepository {

  private final UserJpaRepository jpaRepository;

  @Override
  public User save(User user) {
    return jpaRepository.save(user);
  }

  @Override
  public void delete(User user) {
    jpaRepository.delete(user);
  }

  @Override
  public List<User> findAll() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<User> findById(UUID userId) {
    return jpaRepository.findById(userId);
  }

  @Override
  public List<User> findAllById(List<UUID> userIds) {
    return jpaRepository.findAllById(userIds);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return jpaRepository.findByUsername(username);
  }

  @Override
  public boolean existsByUsername(String username) {
    return jpaRepository.existsByUsername(username);
  }

  @Override
  public boolean existsByEmail(Email email) {
    return jpaRepository.existsByEmail(email);
  }
}
