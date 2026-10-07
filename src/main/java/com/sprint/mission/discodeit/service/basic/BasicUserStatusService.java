package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.common.exception.userstatus.UserStatusAlreadyExistsException;
import com.sprint.mission.discodeit.common.exception.userstatus.UserStatusNotFoundByUserException;
import com.sprint.mission.discodeit.common.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final UserStatusMapper userStatusMapper;

  @Override
  public UserStatusDto create(UserStatusCreateRequest request) {
    UUID userId = request.userId();
    User target = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    //필요없음, 위에서 유저를 가져오기 때문에
//    if (!userRepository.existsById(userId)) {
//      throw new NoSuchElementException("User with id " + userId + " does not exist");
//    }
    if (userStatusRepository.findByUserId(userId).isPresent()) {
      throw new UserStatusAlreadyExistsException(userId);
    }

    Instant lastActiveAt = request.lastActiveAt();
    UserStatus userStatus = new UserStatus(target, lastActiveAt);
    return userStatusMapper.toDto(userStatusRepository.save(userStatus));
  }

  @Override
  public UserStatusDto find(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId)
        .map(userStatusMapper::toDto)
        .orElseThrow(() -> new UserStatusNotFoundException(userStatusId));
  }

  @Override
  public List<UserStatusDto> findAll() {
    return userStatusRepository.findAll().stream()
        .map(userStatusMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new UserStatusNotFoundException(userStatusId));
    userStatus.update(newLastActiveAt);

//    return userStatusRepository.save(userStatus); <- dirty checking으로 자동 업데이트 쿼리 날림
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  @Transactional
  public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
    Instant newLastActiveAt = request.newLastActiveAt();

    UserStatus userStatus = userStatusRepository.findByUserId(userId)
        .orElseThrow(() -> new UserStatusNotFoundByUserException(userId));
    userStatus.update(newLastActiveAt);

//    return userStatusRepository.save(userStatus);
    return userStatusMapper.toDto(userStatus);
  }

  @Override
  public void delete(UUID userStatusId) {
    UserStatus userStatus = userStatusRepository.findById(userStatusId)
        .orElseThrow(() -> new UserStatusNotFoundException(userStatusId));
    userStatusRepository.delete(userStatus);
  }
}
