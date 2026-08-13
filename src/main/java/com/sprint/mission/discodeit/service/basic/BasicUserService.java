package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;import com.sprint.mission.discodeit.entity.BinaryContent;import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    UserRepository userRepository;
    BinaryContentRepository binaryContentRepository;
    UserStatusRepository userStatusRepository;

    @Override
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("이미 사용 중인 유저네임입니다.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        User user = new User(request.username(), request.password(), request.email());

        if (request.profileImageBytes() != null){
            BinaryContent profileImage = new BinaryContent(
                    request.profileImageBytes(),
                    request.profileImageFileName(),
                    request.profileImageContentType()
            );
            binaryContentRepository.save(profileImage);
            user.updateProfile(profileImage.getId());
        }
        userRepository.save(user);
        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);
        return toResponse(user, userStatus);
    }

    @Override
    public UserResponse find(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("유저 상태 정보를 찾을 수 없습니다."));

        return toResponse(user, userStatus);
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                        .orElse(null);
                    return toResponse(user, userStatus);
                })
                .toList();
    }

    @Override
    public UserResponse update(UserUpdateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

        user.update(request.username(), request.email(), request.password());

        if (request.profileImageBytes() != null){
            if (user.getProfileId() != null) {
                binaryContentRepository.deleteById(user.getProfileId());
            }
            BinaryContent newProfileImage = new BinaryContent(
                    request.profileImageBytes(),
                    request.profileImageFileName(),
                    request.profileImageContentType()
            );
            binaryContentRepository.save(newProfileImage);
            user.updateProfile(newProfileImage.getId());
        }

        userRepository.save(user);
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("유저 상태 정보를 찾을 수 없습니다."));

        return toResponse(user, userStatus);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("유저를 찾을 수 없습니다."));

        if (user.getProfileId() != null){
            binaryContentRepository.deleteById(user.getProfileId());
        }

        userStatusRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }

    private UserResponse toResponse(User user, UserStatus userStatus) {
        boolean isOnline = (userStatus != null) && userStatus.isOnline();

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getProfileId(),
                isOnline
        );
    }
}
