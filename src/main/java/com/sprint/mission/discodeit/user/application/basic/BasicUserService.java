package com.sprint.mission.discodeit.user.application.basic;

import com.sprint.mission.discodeit.binaryContent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.user.dto.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.dto.UserUpdateRequestDto;
import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.common.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.common.exception.DuplicateUsernameException;
import com.sprint.mission.discodeit.common.exception.UserNotFoundException;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import com.sprint.mission.discodeit.user.application.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto create(UserCreateRequestDto userRequestDto, MultipartFile profile) {

        //findByUsername, findByEmail 구현하기
        if (this.findByUsername(userRequestDto.username()).isPresent()) {
            log.warn("사용자 생성 실패 - 중복 username: username = {}", userRequestDto.username());
            throw new DuplicateUsernameException(userRequestDto.username());
        }
        if (this.findByEmail(userRequestDto.email()).isPresent()) {
            log.warn("사용자 생성 실패 - 중복 email: email = {}", userRequestDto.email());
            throw new DuplicateEmailException(userRequestDto.email());
        }

        User user = User.create(userRequestDto.username(), userRequestDto.email(), userRequestDto.password());

        // 사진이 있으면
        BinaryContent binaryContent;
        if (profile != null) {
            binaryContent = new BinaryContent(profile.getOriginalFilename(),
                    profile.getSize(),
                    profile.getContentType());
            binaryContentRepository.save(binaryContent);
            try {
                binaryContentStorage.put(binaryContent.getId(), profile.getBytes());
            } catch (IOException e) {
                log.error("사용자 생성 중 프로필 이미지 저장 실패 - username = {}", userRequestDto.username(), e);
                throw new UncheckedIOException(e);
            }
            user.updateProfile(binaryContent);

        }
        UserStatus userStatus = UserStatus.create(user);
        user.updateUserStatus(userStatus);

        userRepository.save(user);  // 영속성 전이로 자식까지 넣음

        log.info("사용자 생성 성공 - userId = {}, username = {}", user.getId(), user.getUserName());
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto find(UUID id) {
        User user = userCheck(id);
        log.debug("사용자 조회 성공 - userId = {}", id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {

        List<UserDto> users = userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
        log.debug("사용자 목록 조회 성공 - count = {}", users.size());
        return users;
    }

    @Override
    @Transactional
    public UserDto update(UUID id, UserUpdateRequestDto userUpdateRequestDto, MultipartFile profile) {
        User user = userCheck(id);
        // 사진이 있으면
        if (profile != null) {

            BinaryContent binaryContent = new BinaryContent(profile.getOriginalFilename(),
                    profile.getSize(),
                    profile.getContentType());

            user.updateProfile(binaryContent);
            binaryContentRepository.save(binaryContent);
            try {
                binaryContentStorage.put(binaryContent.getId(), profile.getBytes());
            } catch (IOException e) {
                log.error("사용자 수정 중 프로필 이미지 저장 실패 - userId = {}", id, e);
                throw new UncheckedIOException(e);
            }


        }

        user.update(userUpdateRequestDto.newUsername(), userUpdateRequestDto.newEmail(), userUpdateRequestDto.newPassword());

        log.info("사용자 수정 성공 - userId = {}, profileChanged = {}", id, profile != null);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        User user = userCheck(id);
        // 프로필 삭제
//        if(user.getProfile() != null) {
//            binaryContentRepository.deleteById(user.getProfile().getId());
//        }
        // 유저 상태 삭제
//        userStatusRepository.deleteByUserId(user.getId());
        // 유저 삭제
        userRepository.deleteById(id);
        log.info("사용자 삭제 성공 - userId = {}", id);
    }


    private User userCheck(UUID id) {

        return userRepository.findById(id).orElseThrow(() -> {
            log.warn("사용자 찾기 실패 - 존재하지 않는 사용자: userId = {}", id);
            return new UserNotFoundException(id);
        });
    }
}
