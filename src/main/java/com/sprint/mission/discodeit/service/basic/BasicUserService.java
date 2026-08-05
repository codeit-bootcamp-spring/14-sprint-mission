package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.dto.user.UserCreationDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
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
    private final ReadStatusRepository readStatusRepository;

    @Override
    public User createAccount(UserCreationDto dto) {
        // 1. 선택적으로 프로필 이미지를 등록할 수 있어야 한다.
        // 다른 필드들은 필수로 받아야한다는 뜻?
        String name = dto.getName();
        String email = dto.getEmail();

        // 2. username과 email은 다른 유저와 달라야 한다.
        if(userRepository.existsByNameOrEmail(name, email)) {
            throw new CustomException(ExceptionType.USER_UNIQUE_FIELD_CONFLICT);
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
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE));

        UserStatus userStatus = userStatusRepository.findByUserId(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USERSTATUS_NOT_FOUND_IN_DATABASE));

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
        if (!userRepository.existsById(id)) {
            throw new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE);
        }

        String name = dto.getName();
        String email = dto.getEmail();
        String password = dto.getPassword();
        UUID profileId = dto.getProfileId();

        userRepository.update(id, name, email, password, profileId);

    }

    @Override
    public void deleteAccount(UUID id) {
        User toBeDeleted = userRepository.findById(id)
                        .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE));
        UUID profileId = toBeDeleted.getProfileId();

        // binary content가 Null일 수도 있어서 여기서 검사했는데 마음에 안듦
        // 이게 최선..?
        if (Objects.nonNull(profileId)) {
            binaryContentRepository.deleteById(toBeDeleted.getProfileId());
        }
        readStatusRepository.deleteByUserId(id);
        channelRepository.deleteUsersByUserId(id);
        messageRepository.deleteAllByUserId(id);
        userStatusRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }
}
