package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserStatusMapper userStatusMapper;

    @Transactional(readOnly = true)
    @Override
    public UserStatusDto find(UUID userStatusId) {
        return userStatusMapper.toDto(getUserStatus(userStatusId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserStatusDto> findAll() {
        return userStatusRepository.findAll().stream()
            .map(userStatusMapper::toDto)
            .toList();
    }

    @Transactional
    @Override
    public UserStatusDto update(UUID userStatusId, UserStatusUpdateRequest request) {
        UserStatus userStatus = getUserStatus(userStatusId);
        userStatus.update(request.newLastActiveAt());
        return userStatusMapper.toDto(userStatus);
    }

    @Transactional
    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND,
                "UserStatus를 찾을 수 없습니다. userId: " + userId));
        userStatus.update(request.newLastActiveAt());
        return userStatusMapper.toDto(userStatus);
    }

    private UserStatus getUserStatus(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_STATUS_NOT_FOUND,
                "UserStatus를 찾을 수 없습니다. id: " + userStatusId));
    }
}
