package com.sprint.mission.discodeit.userstatus.service;

import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusResponseDto;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserStatusServiceImpl implements UserStatusService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    public UserStatusServiceImpl(UserRepository userRepository,
        UserStatusRepository userStatusRepository) {
        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
    }

    @Override
    public UserStatusResponseDto userStatusCreate(
        UserStatusCreateRequestDto userStatusCreateRequestDto) {
        userRepository.findByUser(userStatusCreateRequestDto.userId())
            .orElseThrow(() -> new IllegalArgumentException(
                "존재하지 않는 유저입니다: " + userStatusCreateRequestDto.userId()));

        if (userStatusRepository.findByUserId(userStatusCreateRequestDto.userId()).isPresent()) {
            throw new IllegalArgumentException(
                "이미 상태를 정의한 유저입니다: " + userStatusCreateRequestDto.userId());
        }

        return UserStatusResponseDto.from(
            userStatusRepository.statusAdd(new UserStatus(userStatusCreateRequestDto.userId())));
    }

    @Override
    public UserStatusResponseDto userStatusUpdate(UUID userStatusId,
        UserStatusUpdateRequestDto userStatusUpdateRequestDto) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId);

        if (userStatusUpdateRequestDto.userId() != null) {
            userStatus.updateUserId(userStatusUpdateRequestDto.userId());
        }
        if (userStatusUpdateRequestDto.lastActiveAt() != null) {
            userStatus.updateAt(userStatusUpdateRequestDto.lastActiveAt());
        }

        userStatusRepository.update(userStatus);

        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public void userStatusDelete(UUID userStatusId) {
        UserStatus userStatus = userStatusRepository.findById(userStatusId);
        userStatusRepository.delete(userStatus);
    }

    @Override
    public List<UserStatusResponseDto> findAllByUserId(UUID userId) {
        List<UserStatus> userStatuses = userStatusRepository.findAll();

        return userStatuses.stream()
            .map(UserStatusResponseDto::from)
            .toList();
    }

    @Override
    public UserStatusResponseDto findUserStatus(UUID userStatusId) {
        return UserStatusResponseDto.from(userStatusRepository.findById(userStatusId));
    }
}
