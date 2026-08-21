package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.*;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ReadStatusRepository readStatusRepository;

    public UserDto createAccount(String name,
                                 String email,
                                 String password,
                                 @Nullable MultipartFile profile) {
        // 1. 선택적으로 프로필 이미지를 등록할 수 있어야 한다.
        // 2. username과 email은 다른 유저와 달라야 한다.
        if (userRepository.existsByNameOrEmail(name, email)) {
            throw new CustomException(ExceptionType.USER_UNIQUE_FIELD_CONFLICT);
        }

        UUID profileId = binaryContentRepository.create(new BinaryContent(profile)).getId();
        // 3. UserStatus를 같이 생성해야 한다.
        User user = new User(name, email, password, profileId);
        UserStatus userStatus = userStatusRepository.create(new UserStatus(user.getId()));
        User created = userRepository.create(user);
        return UserDto.of(created, userStatus);
    }


    public UserDto getUser(UUID id) {
        // 1. 사용자 온라인 정보를 포함시켜야 한다.
        // 2. 패스워드 정보는 제외해야 한다.
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE));

        UserStatus userStatus = userStatusRepository.findByUserId(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USERSTATUS_NOT_FOUND_IN_DATABASE));

        return UserDto.of(user, userStatus);
    }

    public List<UserDto> getAllUsers() {
        // 1. 사용자 온라인 정보를 포함시켜야 한다.
        // 2. 패스워드 정보는 제외해야 한다.
        List<User> users = userRepository.findAll();
        List<UserDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(getUser(user.getId()));
        }

        return dtos;
    }

    public UserDto updateUser(UUID id,
                              String name, String email, String password,
                              @Nullable MultipartFile profile) {
        if (userRepository.existsByNameOrEmail(name, email)) {
            throw new CustomException(ExceptionType.USER_UNIQUE_FIELD_CONFLICT);
        }

        // 1. 선택적으로 프로필 이미지를 대체할 수 있어야 한다.
        // 2. DTO를 활용해 파라미터를 그룹화한다.
        User updating = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE));

        UUID profileId = (Objects.nonNull(profile)) ?
                binaryContentRepository.create(new BinaryContent(profile)).getId() : null;



        userRepository.update(id, name, email, password, profileId);
        User updated = userRepository.findById(id).orElseThrow();
        UserStatus userStatus = userStatusRepository.findByUserId(id).orElseThrow();
        return UserDto.of(updated, userStatus);
    }

    public UserDto deleteAccount(UUID id) {
        User deleted = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE));
        UserStatus userStatus = userStatusRepository.findByUserId(id).orElseThrow();
        UUID profileId = deleted.getProfileId();

        // binary content가 Null일 수도 있어서 여기서 검사했는데 마음에 안듦
        // 이게 최선..?
        if (Objects.nonNull(profileId)) {
            binaryContentRepository.deleteById(deleted.getProfileId());
        }
        readStatusRepository.deleteByUserId(id);
        messageRepository.deleteAllByUserId(id);
        userStatusRepository.deleteByUserId(id);
        userRepository.deleteById(id);

        return UserDto.of(deleted, userStatus);
    }
}
