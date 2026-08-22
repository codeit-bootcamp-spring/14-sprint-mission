package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserRequest;
import com.sprint.mission.discodeit.dto.userDto.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JCFUserService implements UserService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;


    @Override
    public UserResponse create(UserCreateRequest userCreateRequest) {
        UserCreateRequest validRequest = validateRequest(userCreateRequest);
        User user = User.create(validRequest);
        UserStatus userStatus = UserStatus.create(user);
        UserStatus savedUserStatus = userStatusRepository.save(userStatus);
        User savedUser = userRepository.save(user);
        return UserResponse.from(savedUser, savedUserStatus);
    }

    @Override
    public UserResponse findById(UUID uuid) {
        User user = uuidValidator(uuid);
        UserStatus userStatus = getUserStatusByUserId(uuid);
        return UserResponse.from(user, userStatus);
    }


    @Override
    public UserResponse update(UUID id, UserRequest userRequest) {
        User updatedUser = uuidValidator(id).update(
            userRequest.name(),
            userRequest.password(),
            userRequest.email());
        User savedUser = userRepository.save(updatedUser);
        UserStatus userStatus = getUserStatusByUserId(savedUser.getId()).refresh();
        UserStatus savedUserStatus = userStatusRepository.save(userStatus);
        return UserResponse.from(savedUser, savedUserStatus);
    }

    @Override
    public void delete(UUID uuid) {
        uuidValidator(uuid);
        UserStatus userStatus = getUserStatusByUserId(uuid);
        userRepository.delete(uuid);
        UUID userStatusUUID = userStatus.getUuid();
        userStatusRepository.delete(userStatusUUID);
    }

    @Override
    public List<UserResponse> readAll() {
        return userRepository.readAll().stream()
            .map(user -> UserResponse.from(
                user, getUserStatusByUserId(user.getId()))).toList();

    }

    private User uuidValidator(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(()
            -> new DiscodeitRuntimeException(ExceptionType.USER_NOT_FOUND));
    }

    private UserCreateRequest validateRequest(UserCreateRequest userCreateRequest) {
        String name = userCreateRequest.name();
        String email = userCreateRequest.email();
        List<User> userList = userRepository.readAll();
        boolean result = userList.stream()
            .noneMatch(m -> m.getEmail().equals(email) || m.getName().equals(name));
        if (!result) {
            throw new DiscodeitRuntimeException(ExceptionType.USER_ALREADY_EXIST);
        }
        return userCreateRequest;
    }

    private UserStatus getUserStatusByUserId(UUID uuid) {
        return userStatusRepository.readAll().stream()
            .filter(status -> status.getUserUuid().equals(uuid))
            .findAny().orElseThrow(() -> new DiscodeitRuntimeException(ExceptionType.USER_ALREADY_EXIST));
    }
}