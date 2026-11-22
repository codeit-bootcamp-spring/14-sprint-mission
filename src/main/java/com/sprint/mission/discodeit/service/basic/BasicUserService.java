package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
        log.debug("사용자 생성 시작: username={}, email={}", request.username(), request.email());
        validateDuplicateUsername(request.username());
        validateDuplicateEmail(request.email());

        BinaryContent profile = saveProfile(profileRequest);
        User user = new User(request.username(), request.email(), request.password(), profile);
        new UserStatus(user, Instant.now());

        userRepository.save(user);
        log.info("사용자 생성 완료: userId={}, username={}", user.getId(), user.getUsername());
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
        log.debug("사용자 수정 시작: userId={}", userId);
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

        log.info("사용자 수정 완료: userId={}", userId);
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        log.debug("사용자 삭제 시작: userId={}", userId);
        userRepository.delete(getUser(userId));
        log.info("사용자 삭제 완료: userId={}", userId);
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
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
            throw new UserAlreadyExistsException("username", username);
        }
    }

    private void validateDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("email", email);
        }
    }
}
