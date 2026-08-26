package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusDto create(UserStatusCreateRequest request) {
        userRepository.findById(request.userId())
            .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND, "해당 유저를 찾을 수 없습니다. 유저ID(" + request.userId() + ")"));

        userStatusRepository.findByUserId(request.userId())
            .ifPresent(status -> {
                throw new DiscodeitException(ErrorCode.DUPLICATE_USER_STATUS
                    , "유저 상태 중복됨" + request.userId());
            });

        UserStatus userStatus = UserStatus.builder()
            .userId(request.userId())
            .build();
        userStatusRepository.save(userStatus);
        return toDto(userStatus);
    }

    @Override
    public Optional<UserStatusDto> find(UUID id) {
        return userStatusRepository.findById(id)
            .map(BasicUserStatusService::toDto);
    }


    @Override
    public List<UserStatusDto> findAll() {
        List<UserStatus> userStatuses = userStatusRepository.findAll();

        List<UserStatusDto> result = new ArrayList<>();
        for (UserStatus userStatus : userStatuses) {
            result.add(toDto(userStatus));

        }
        return result;
    }

    @Override
    public UserStatusDto update(UUID id, UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findById(id)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND,
                "UserStatus를 찾을 수 없습니다. id:" + id));

        userStatus.updateLastActiveAt(request.lastActiveAt());
        userStatusRepository.update(userStatus);
        return toDto(userStatus);
    }

    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND,
                "UserStatus를 찾을 수 없습니다. id:" + userId));

        userStatus.updateLastActiveAt(request.lastActiveAt());
        userStatusRepository.update(userStatus);
        return toDto(userStatus);
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.findById(id)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND,
                "UserStatus를 찾을 수 없습니다. id:" + id));

        userStatusRepository.delete(id);

    }

    private static UserStatusDto toDto(UserStatus userStatus) {
        return new UserStatusDto(
            userStatus.getId(),
            userStatus.getUserId(),
            userStatus.getLastActiveAt()
        );
    }


}
