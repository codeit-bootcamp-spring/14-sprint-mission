package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserResponse create(UserCreateRequest request) {
        List<User> allUsers = userRepository.findAll();
        for(User existUser : allUsers) {
            if(existUser.getUsername().equals(request.getUsername())) {
                throw new NoSuchElementException("사용중인 ID");
            }
            if(existUser.getEmail().equals(request.getEmail())) {
                throw new NoSuchElementException("사용중인 Email");
            }
        }

        if(request.getProfileImageId() != null) {
            if(binaryContentRepository.findById(request.getProfileImageId()).isEmpty()) {
                throw new NoSuchElementException("프로필 이미지 누락.");
            }
        }

        User user = new User(request.getUsername(), request.getEmail(), request.getPassword(), request.getProfileImageId());
        User savedUser = userRepository.save(user);

        UserStatus userStatus = new UserStatus(savedUser.getId(), Instant.now());
        userStatusRepository.save(userStatus);

        return new UserResponse(savedUser, userStatus);
    }

    @Override
    public UserResponse find(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElse(null);

        return new UserResponse(user, userStatus);
    }

    @Override
    public List<User> findAll() {
    }

    @Override
    public UserResponse update(UUID userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        if(request.getProfileImageId() != null) {
            boolean imageExist = binaryContentRepository.findById(request.getProfileImageId()).isPresent();
            if(!imageExist) {
                throw new NoSuchElementException("프로필 이미지 누락.");
            }
        }

        user.update(request.getUsername(), request.getEmail(), request.getPassword(), request.getProfileImageId());
        User updatedUser = userRepository.save(user);

        UserStatus userStatus = userStatusRepository.findByUserId(userId).orElse(null);

        return new UserResponse(updatedUser, userStatus);
    }

    @Override
    public void delete(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        userStatusRepository.deleteByUserId(userId);

        if(user.getProfileId() != null) {
            binaryContentRepository.delete(user.getProfileId());
        }

        userRepository.deleteById(user.getId());
    }
}