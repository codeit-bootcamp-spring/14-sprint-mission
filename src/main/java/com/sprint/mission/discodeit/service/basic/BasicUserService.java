package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  //
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  @Transactional
  public User create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("User with email " + email + " already exists");
    }
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("User with username " + username + " already exists");
    }

    BinaryContent profile = optionalProfileCreateRequest
        .map(request -> binaryContentRepository.save(
            new BinaryContent(
                request.fileName(),
                (long) request.bytes().length,
                request.contentType(),
                request.bytes()
            )
        ))
        .orElse(null);

    User createdUser = userRepository.save(
        new User(username, email, userCreateRequest.password(), profile)
    );

    userStatusRepository.save(new UserStatus(createdUser, Instant.now()));
    return createdUser;
  }

  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(this::toDto)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  @Transactional
  public User update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    if (userRepository.existsByEmail(newEmail)) {
      throw new IllegalArgumentException("User with email " + newEmail + " already exists");
    }
    if (userRepository.existsByUsername(newUsername)) {
      throw new IllegalArgumentException("User with username " + newUsername + " already exists");
    }

    BinaryContent profile = optionalProfileCreateRequest
        .map(request -> {
          Optional.ofNullable(user.getProfile())
              .map(BinaryContent::getId)
              .ifPresent(binaryContentRepository::deleteById);

          return binaryContentRepository.save(
              new BinaryContent(
                  request.fileName(),
                  (long) request.bytes().length,
                  request.contentType(),
                  request.bytes()
              )
          );
        })
        .orElse(null);

    user.update(newUsername, newEmail, userUpdateRequest.newPassword(), profile);
   /* 트랜잭션 사용해서 컨텍스트내에서 기존값과 새로운값을 비교 <- drity checking
    return userRepository.save(user);
    커밋시 dirty checking에서 값이 바뀌었따면 업데이트 쿼리 날림<- .save() 생략가능 */
    return user; // <- 이거 실행되고 커밋, db에 업데이트 쿼리 날림 @Transacitonal 덕분
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    BinaryContent profile = user.getProfile();
    if (profile != null) {
      binaryContentRepository.deleteById(profile.getId());
    }

    userStatusRepository.deleteByUserId(userId);
    userRepository.deleteById(userId);
  }

  private UserDto toDto(User user) {
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline)
        .orElse(null);

    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfile().getId(),
        online
    );
  }
}
