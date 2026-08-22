package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.UserStatusDto;
import com.sprint.mission.discodeit.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;

    public BasicUserStatusService(UserStatusRepository userStatusRepository) {
        this.userStatusRepository = userStatusRepository;
    }

    @Override
    public UserStatusDto create(UserStatusCreateRequest request) {
        // 유저 하나에 상태 하나. 이미 있으면 update를 쓰라고 안내한다.
        userStatusRepository.findByUserId(request.userId()).ifPresent(existing -> {
            throw new DiscodeitException(ExceptionType.USER_STATUS_ALREADY_EXISTS,
                    "이미 상태가 등록된 유저입니다! userId: " + request.userId());
        });

        UserStatus userStatus = new UserStatus(request.userId(), request.lastActiveAt());
        return toDto(userStatusRepository.save(userStatus));
    }

    @Override
    public UserStatusDto find(UUID userStatusId) {
        return toDto(findEntity(userStatusId));
    }

    @Override
    public List<UserStatusDto> findAll() {
        return userStatusRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
        UserStatus userStatus = findEntity(userStatusId);
        userStatus.updateLastActiveAt(request.newLastActiveAt());
        return toDto(userStatusRepository.save(userStatus));
    }

    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new DiscodeitException(ExceptionType.USER_STATUS_NOT_FOUND,
                        "유저 상태를 찾을 수 없습니다! userId: " + userId));
        userStatus.updateLastActiveAt(request.newLastActiveAt());
        return toDto(userStatusRepository.save(userStatus));
    }

    @Override
    public void delete(UUID userStatusId) {
        findEntity(userStatusId);
        userStatusRepository.deleteById(userStatusId);
    }

    private UserStatus findEntity(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new DiscodeitException(ExceptionType.USER_STATUS_NOT_FOUND,
                        "유저 상태를 찾을 수 없습니다! id: " + userStatusId));
    }

    private UserStatusDto toDto(UserStatus userStatus) {
        return new UserStatusDto(
                userStatus.getId(),
                userStatus.getCreatedAt(),
                userStatus.getUpdatedAt(),
                userStatus.getUserId(),
                userStatus.getLastActiveAt(),
                userStatus.isOnline()
        );
    }
}
