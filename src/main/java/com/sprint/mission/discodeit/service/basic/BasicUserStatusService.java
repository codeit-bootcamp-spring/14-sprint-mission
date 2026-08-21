package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatusdto.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userstatusdto.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userstatusdto.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.CustomRuntimeException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusResponseDto createUserStatus(UserStatusCreateRequestDto requestDto) {
        if (Objects.isNull(userRepository.findById(requestDto.getUserId()))) {
//            throw new RuntimeException("관련된 User가 존재하지 않습니다: " );
            throw new CustomRuntimeException(ExceptionType.NOT_FOUND);
        }

        if (Objects.nonNull(userStatusRepository.findByUserId(requestDto.getUserId()))) {
//            throw new RuntimeException("해당 User에 대한 UserStatus가 이미 존재합니다.");
            throw new CustomRuntimeException(ExceptionType.DATABASE_CONNECTION_FAILED);
        }

        UserStatus userStatus = requestDto.toEntity();
        userStatusRepository.save(userStatus);
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public UserStatusResponseDto readUserStatus(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id);
        if (Objects.isNull(userStatus)) {
//            throw new RuntimeException("해당 UserStatus가 존재하지 않습니다: ");
            throw new CustomRuntimeException(ExceptionType.NOT_FOUND);
        }
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public List<UserStatusResponseDto> readAllUserStatus() {
        return userStatusRepository.findAll().stream()
                .map(each -> UserStatusResponseDto.from(each))
                .toList();
    }

    @Override
    public UserStatusResponseDto updateUserStatus(UUID id, UserStatusUpdateRequestDto requestDto) {
        UserStatus target = userStatusRepository.findById(id);
        if (Objects.isNull(target)) {
//            throw new RuntimeException("해당 UserStatus가 존재하지 않습니다: " );
            throw new CustomRuntimeException(ExceptionType.NOT_FOUND);
        }
        if (requestDto.getLastActiveAt() != null) {
            target.setLastActiveAt(requestDto.getLastActiveAt());
        } else {
            target.setLastActiveAt();
        }
        target.setUpdatedAt();
        userStatusRepository.save(target);
        return UserStatusResponseDto.from(target);
    }

    @Override
    public UserStatusResponseDto updateUserStatusByUserId(UUID userId, UserStatusUpdateRequestDto requestDto) {
        UserStatus target = userStatusRepository.findByUserId(userId);
        if (Objects.isNull(target)) {
//            throw new RuntimeException("해당 User의 UserStatus가 존재하지 않습니다: " );
            throw new CustomRuntimeException(ExceptionType.NOT_FOUND);
        }
        if (requestDto.getLastActiveAt() != null) {
            target.setLastActiveAt(requestDto.getLastActiveAt());
        } else {
            target.setLastActiveAt();
        }
        target.setUpdatedAt();
        userStatusRepository.save(target);
        return UserStatusResponseDto.from(target);
    }

    @Override
    public void deleteUserStatus(UUID id) {
        userStatusRepository.delete(id);
    }
}
