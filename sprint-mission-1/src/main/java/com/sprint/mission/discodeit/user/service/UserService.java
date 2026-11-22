package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.binarycontent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ExceptionType;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.readstatus.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto userCreate(UserCreateRequestDto userCreateRequestDto,
        MultipartFile profile) {
        if (userRepository.findByUserName(userCreateRequestDto.username()).isPresent()) {
            throw new DiscodeitException(
                ExceptionType.USER_NAME_CONFLICT,
                Map.of("userName", userCreateRequestDto.username())
            );
        }

        if (userRepository.findByEmail(userCreateRequestDto.email()).isPresent()) {
            throw new DiscodeitException(
                ExceptionType.USER_EMAIL_CONFLICT,
                Map.of("userEmail", userCreateRequestDto.email())
            );
        }

        BinaryContent binaryContent = null;
        if (profile != null) {
            binaryContent = binaryContentRepository.save(
                new BinaryContent(
                    Objects.requireNonNull(profile.getOriginalFilename()),
                    profile.getContentType(), profile.getSize()));
            try {
                binaryContentStorage.put(binaryContent.getId(), profile.getBytes());
            } catch (IOException e) {
                throw new DiscodeitException(
                    ExceptionType.FILE_SAVE_FAILED,
                    Map.of("binaryContentId", binaryContent.getId()),
                    e);
            }
        }

        User user = userRepository.save(
            User.create(userCreateRequestDto.username(),
                passwordEncoder.encode(userCreateRequestDto.password()),
                userCreateRequestDto.email(), binaryContent));

        log.info("사용자 생성 완료: userId={}", user.getId());
        return userMapper.toDto(user);
    }

    // 본인만 수정할 수 있다. #userId는 이 메서드의 파라미터 이름과 같아야 한다
    @Transactional
    @PreAuthorize("#userId == authentication.principal.userDto.id")
    public UserDto userUpdate(UUID userId, UserUpdateRequestDto userUpdateRequestDto,
        MultipartFile profile) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_NOT_FOUND,
                Map.of("userId", userId)
            ));

        if (profile != null) {
            BinaryContent binaryContent = binaryContentRepository.save(
                new BinaryContent(
                    Objects.requireNonNull(profile.getOriginalFilename()),
                    profile.getContentType(), profile.getSize()));
            try {
                binaryContentStorage.put(binaryContent.getId(), profile.getBytes());
            } catch (IOException e) {
                throw new DiscodeitException(
                    ExceptionType.FILE_SAVE_FAILED,
                    Map.of("binaryContentId", binaryContent.getId()),
                    e);
            }
            user.updateProfile(binaryContent);
        }

        user.update(userUpdateRequestDto.newUsername(),
            passwordEncoder.encode(userUpdateRequestDto.newPassword()),
            userUpdateRequestDto.newEmail());

        log.info("사용자 수정 완료: userId={}", user.getId());
        return userMapper.toDto(user);
    }

    @Transactional
    @PreAuthorize("#userId == authentication.principal.userDto.id")
    public void userDelete(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_NOT_FOUND,
                Map.of("userId", userId)
            ));

        readStatusRepository.deleteAllByUser(user);
        messageRepository.clearAuthor(user);
        userRepository.delete(user);

        log.info("사용자 삭제 완료: userId={}", user.getId());
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
            .map(userMapper::toDto)
            .toList();
    }

    public UserDto findById(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_NOT_FOUND,
                Map.of("userId", userId)
            ));

        return userMapper.toDto(user);
    }
}
