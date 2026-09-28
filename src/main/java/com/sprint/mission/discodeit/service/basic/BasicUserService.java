package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserDto create(UserCreateRequest request, BinaryContentCreateRequest profileRequest) {
        validateDuplicateUsername(request.username());
        validateDuplicateEmail(request.email());

        BinaryContent profile = saveProfile(profileRequest);
        User user = new User(request.username(), request.email(), request.password(), profile);
        new UserStatus(user, Instant.now());

        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto find(UUID userId) {
        return userMapper.toDto(getUser(userId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
            .map(userMapper::toDto)
            .toList();
    }

    @Transactional
    @Override
    public UserDto update(UUID userId, UserUpdateRequest request,
        BinaryContentCreateRequest profileRequest) {
        User user = getUser(userId);

        if (request.newUsername() != null && !request.newUsername().equals(user.getUsername())) {
            validateDuplicateUsername(request.newUsername());
        }
        if (request.newEmail() != null && !request.newEmail().equals(user.getEmail())) {
            validateDuplicateEmail(request.newEmail());
        }
        user.update(request.newUsername(), request.newEmail(), request.newPassword());

        if (profileRequest != null) {
            user.updateProfile(saveProfile(profileRequest));
        }

        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        userRepository.delete(getUser(userId));
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND,
                "해당 유저를 찾을 수 없습니다. userId: " + userId));
    }

    private BinaryContent saveProfile(BinaryContentCreateRequest profileRequest) {
        if (profileRequest == null) {
            return null;
        }
        BinaryContent profile = new BinaryContent(
            profileRequest.fileName(), (long) profileRequest.bytes().length,
            profileRequest.contentType());
        binaryContentRepository.save(profile);
        binaryContentStorage.put(profile.getId(), profileRequest.bytes());
        return profile;
    }

    private void validateDuplicateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new DiscodeitException(ErrorCode.DUPLICATE_USERNAME,
                "이미 사용 중인 username입니다: " + username);
        }
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DiscodeitException(ErrorCode.DUPLICATE_EMAIL,
                "이미 사용 중인 email입니다: " + email);
        }
    }
}
