package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * 다른 Service 대신 필요한 Repository만 주입받는다.
 * 서비스끼리 물리면 Spring이 생성 순서를 정하지 못해 앱이 아예 뜨지 않는다(순환 참조).
 */
@Service
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    public BasicUserService(
            UserRepository userRepository,
            BinaryContentRepository binaryContentRepository,
            UserStatusRepository userStatusRepository
    ) {
        this.userRepository = userRepository;
        this.binaryContentRepository = binaryContentRepository;
        this.userStatusRepository = userStatusRepository;
    }

    @Override
    public UserDto create(UserCreateRequest request, Optional<BinaryContentCreateRequest> profileRequest) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DiscodeitException(ExceptionType.DUPLICATE_USERNAME,
                    "이미 사용 중인 username입니다: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DiscodeitException(ExceptionType.DUPLICATE_EMAIL,
                    "이미 사용 중인 email입니다: " + request.email());
        }

        UUID profileId = saveProfile(profileRequest).orElse(null);

        User user = new User(request.username(), request.email(), request.password(), profileId);
        userRepository.save(user);

        // 유저는 있는데 상태가 없는 시점을 만들지 않는다.
        userStatusRepository.save(new UserStatus(user.getId(), Instant.now()));

        return toDto(user);
    }

    @Override
    public UserDto find(UUID userId) {
        return toDto(findEntity(userId));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserDto update(UUID userId, UserUpdateRequest request, Optional<BinaryContentCreateRequest> profileRequest) {
        User user = findEntity(userId);

        // 값이 실제로 바뀔 때만 검사한다. 아니면 자기 username으로 수정할 때도 막힌다.
        String newUsername = request.newUsername();
        if (newUsername != null && !newUsername.equals(user.getUsername())
                && userRepository.existsByUsername(newUsername)) {
            throw new DiscodeitException(ExceptionType.DUPLICATE_USERNAME,
                    "이미 사용 중인 username입니다: " + newUsername);
        }
        String newEmail = request.newEmail();
        if (newEmail != null && !newEmail.equals(user.getEmail())
                && userRepository.existsByEmail(newEmail)) {
            throw new DiscodeitException(ExceptionType.DUPLICATE_EMAIL,
                    "이미 사용 중인 email입니다: " + newEmail);
        }

        user.update(newUsername, newEmail, request.newPassword());

        // 교체 시 기존 것을 지운다. 안 지우면 아무도 참조하지 않는 BinaryContent가 남는다.
        saveProfile(profileRequest).ifPresent(newProfileId -> {
            Optional.ofNullable(user.getProfileId())
                    .ifPresent(binaryContentRepository::deleteById);
            user.updateProfileId(newProfileId);
        });

        return toDto(userRepository.save(user));
    }

    @Override
    public void delete(UUID userId) {
        User user = findEntity(userId);

        // 관련된 도메인을 같이 지운다.
        Optional.ofNullable(user.getProfileId())
                .ifPresent(binaryContentRepository::deleteById);
        userStatusRepository.deleteByUserId(userId);

        userRepository.deleteById(userId);
    }

    private User findEntity(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DiscodeitException(ExceptionType.USER_NOT_FOUND,
                        "유저를 찾을 수 없습니다! id: " + userId));
    }

    private Optional<UUID> saveProfile(Optional<BinaryContentCreateRequest> profileRequest) {
        return profileRequest.map(profile -> binaryContentRepository
                .save(new BinaryContent(profile.fileName(), profile.contentType(), profile.bytes()))
                .getId());
    }

    /** 엔티티와 온라인 상태를 합쳐 DTO로 만든다. password는 담지 않는다. */
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
                user.getProfileId(),
                online
        );
    }
}
