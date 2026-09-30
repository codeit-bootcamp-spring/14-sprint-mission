package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.IService.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserResponseDto create(UserCreateRequestDto userRequest, BinaryContentCreateRequestDto profileRequest) {
        if (userRepository.existsByEmail(userRequest.email())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        if (userRepository.existsByUsername(userRequest.username())) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다.");
        }

        BinaryContent profile = null;
        if (profileRequest != null) {
            profile = profileRequest.toEntity();
            binaryContentRepository.save(profile);
        }

        User user = userRequest.toEntity(profile);
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user, Instant.now());
        userStatusRepository.save(userStatus);

        boolean online = userStatus.isOnline();
        return UserResponseDto.from(user, online);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto read(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));

        UserStatus userStatus = userStatusRepository.findByUser_Id(id)
            .orElseThrow(() -> new NoSuchElementException("유저 상태 정보가 없습니다."));

        boolean online = userStatus.isOnline();
        return UserResponseDto.from(user, online);
    }

    @Override
    public UserResponseDto update(UUID id, UserUpdateRequestDto requestDto, BinaryContentCreateRequestDto profileRequest) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));

        user.update(requestDto.newUsername(), requestDto.newEmail(), null);

        if (profileRequest != null) {
            BinaryContent oldProfile = user.getProfile();

            BinaryContent profile = profileRequest.toEntity();
            binaryContentRepository.save(profile);
            user.updateProfile(profile);

            if (oldProfile != null) {
                binaryContentStorage.delete(oldProfile.getId()); // 실제 파일 삭제
                binaryContentRepository.delete(oldProfile);      // DB 레코드도 삭제
            }
        }

        UserStatus userStatus = userStatusRepository.findByUser_Id(id)
            .orElseThrow(() -> new NoSuchElementException("유저 상태 정보가 없습니다."));

        boolean online = userStatus.isOnline();
        return UserResponseDto.from(user, online);
    }

    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
        BinaryContent profile = user.getProfile(); // 유저 삭제 전에 미리 참조 잡아둠

        userRepository.deleteById(id);   // FK 참조 끊기(유저 먼저 삭제)

        if (profile != null) {
            binaryContentStorage.delete(profile.getId());
            binaryContentRepository.delete(profile);
        }

        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> readAll() {
        return userRepository.findAll().stream()
            .map(user -> {
                UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
                    .orElseThrow(() -> new NoSuchElementException("유저 상태 정보가 없습니다."));
                boolean online = userStatus.isOnline();
                return UserResponseDto.from(user, online);
            })
            .toList();
    }

}
