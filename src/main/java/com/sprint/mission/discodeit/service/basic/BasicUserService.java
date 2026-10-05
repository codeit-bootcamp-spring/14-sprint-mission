package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.user.EmailAlreadyExistsException;
import com.sprint.mission.discodeit.common.exception.user.UsernameAlreadyExistsException;
import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  //
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    log.debug("유저 생성 시작 - 입력 이름: {} 입력 이메일: {}", username, email);
    if (userRepository.existsByEmail(email)) {
      log.warn("중복된 이메일 - 입력 이메일: {}", email);
      throw new EmailAlreadyExistsException(email);
    }
    if (userRepository.existsByUsername(username)) {
      log.warn("중복된 사용자 이름 - 입력 이름: {}", username);
      throw new UsernameAlreadyExistsException(username);
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
    log.debug("유저 생성 요청 - 입력 이름: {} 입력 이메일: {}", createdUser.getUsername(), createdUser.getEmail());

    userStatusRepository.save(new UserStatus(createdUser, Instant.now()));
    return toDto(createdUser);
  }

  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(this::toDto)
        .orElseThrow(() -> new UserNotFoundException(userId));
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
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    long requestedFieldCount = Stream.of(
            userUpdateRequest.newUsername(),
            userUpdateRequest.newEmail(),
            userUpdateRequest.newPassword()
        )
        .filter(Objects::nonNull)
        .count();
    log.debug("유저 업데이트 요청 처리 - userId={}, requestedFieldCount={}",
        userId, requestedFieldCount);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("존재하지 않는 유저 조회 - userId: {}", userId);
          return new UserNotFoundException(userId);
        });

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    if (userRepository.existsByEmail(newEmail)) {
      log.warn("이미 존재하는 이메일 - email: {}", newEmail);
      throw new EmailAlreadyExistsException(newEmail);
    }
    if (userRepository.existsByUsername(newUsername)) {
      log.warn("이미 존재하는 이름 - userName: {}", newUsername);
      throw new UsernameAlreadyExistsException(newUsername);
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
    log.debug("유저 업데이트 요청 - 업데이트 된 userId: {}, 변경 된 이름: {}",user.getId(), user.getUsername());
   /* 트랜잭션 사용해서 컨텍스트내에서 기존값과 새로운값을 비교 <- drity checking
    return userRepository.save(user);
    커밋시 dirty checking에서 값이 바뀌었따면 업데이트 쿼리 날림<- .save() 생략가능 */
    return toDto(user); // <- 이거 실행되고 커밋, db에 업데이트 쿼리 날림 @Transacitonal 덕분
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    log.debug("유저 삭제 시작 - 삭제 대상 id: {}", userId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("존재하지 않는 유저 조회 - userId: {}", userId);
          return new UserNotFoundException(userId);
        });

    BinaryContent profile = user.getProfile();
    if (profile != null) {
      binaryContentRepository.deleteById(profile.getId());
    }

    userStatusRepository.deleteByUserId(userId);
    userRepository.deleteById(userId);
    log.debug("유저 삭제 요청 - userId: {}, 유저 이름: {} ", userId, user.getUsername());
  }

  private UserDto toDto(User user) {
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline)
        .orElse(null);

    return userMapper.toDto(user, online);
  }
}
