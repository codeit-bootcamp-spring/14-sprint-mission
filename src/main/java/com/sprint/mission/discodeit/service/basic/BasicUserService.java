package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.dto.user.UserCreationDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public User createAccount(UserCreationDto dto) {
        // 1. 선택적으로 프로필 이미지를 등록할 수 있어야 한다.
        // 다른 필드들은 필수로 받아야한다는 뜻?
        // Controller 안거쳐서 @Valid는 의미 없으니 service에서 노가다
        String name = dto.getName();
        String email = dto.getEmail();
        String password = dto.getPassword();

        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("유저 생성 시 name이 꼭 필요합니다.");
        }
        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("유저 생성 시 email이 꼭 필요합니다.");
        }
        if (Objects.isNull(password)) {
            throw new IllegalArgumentException("유저 생성 시 password가 꼭 필요합니다.");
        }

        // 2. username과 email은 다른 유저와 달라야 한다.
        if(userRepository.existsByNameOrEmail(name, email)) {
            throw new IllegalArgumentException("유저 생성 시 이미 사용 중인 name, email은 사용할 수 없습니다.");
        }

        // 3. UserStatus를 같이 생성해야 한다.
        User user = dto.toUser();
        userStatusRepository.create(new UserStatus(user.getId()));

        return userRepository.create(user);
    }

    @Override
    public UserResponseDto getUser(UUID id) {
        // 1. 사용자 온라인 정보를 포함시켜야 한다.
        // 2. 패스워드 정보는 제외해야 한다.

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("userId %s가 존재하지 않으므로 조회 불가", id)));

        UserStatus userStatus = userStatusRepository.findByUserId(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("userStatusId %s가 존재하지 않으므로 조회 불가", id)));

        return UserResponseDto.of(user, userStatus);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        // 1. 사용자 온라인 정보를 포함시켜야 한다.
        // 2. 패스워드 정보는 제외해야 한다.
        List<User> users = userRepository.findAll();
        List<UserResponseDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(getUser(user.getId()));
        }

        return dtos;
    }

    @Override
    public void updateUser(UUID id, @Valid UserUpdateDto dto) {
        // 1. 선택적으로 프로필 이미지를 대체할 수 있어야 한다.
        // 2. DTO를 활용해 파라미터를 그룹화한다.
        String name = dto.getName();
        String email = dto.getEmail();
        String password = dto.getPassword();
        UUID profileId = dto.getProfileId();

        if (userRepository.existsById(id)) {
            userRepository.update(id, name, email, password, profileId);
        } else {
            throw new NoSuchElementException(String.format("userId %s가 존재하지 않으므로 조회 불가", id));
        }

    }

    @Override
    public void deleteAccount(UUID id) {
        // TODO 1. 관련된 도메인도 같이 삭제한다. (BinaryContent, UserStatus, ReadStatus ... )
        User toBeDeleted = userRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException(String.format("userId %s가 존재하지 않으므로 조회 불가", id)));
        // 여기서 이렇게 if 해서 처리하는게 맘에 안듦
        Optional.ofNullable(toBeDeleted.getProfileId())
                        .ifPresent(profileId -> binaryContentRepository.deleteById(profileId));

        channelRepository.deleteUsersByUserId(id);
        messageRepository.deleteAllByUserId(id);
        userStatusRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }
}
