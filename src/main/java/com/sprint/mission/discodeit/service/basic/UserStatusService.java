package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.IService.IUserStatusService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStatusService implements IUserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    @Override
    public UserStatusResponseDto create(UserStatusCreateRequestDto request) {
        User user = userRepository.findById(request.userId());
        if (Objects.isNull(user)) {
            throw new RuntimeException("존재하지 않는 유저 입니다");
        }
        if (userStatusRepository.existsByUserId(request.userId())) {
            throw new RuntimeException("이미 존재하는 유저 상태 입니다.");
        }
        UserStatus userStatus = request.toEntity();
        userStatusRepository.save(userStatus);

        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public UserStatusResponseDto find(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id);
        if (Objects.isNull(userStatus)) {
            throw new RuntimeException("없는 유저 상태입니다");
        }
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public List<UserStatusResponseDto> findAll() {
        return userStatusRepository.findAll().
            stream()
            .map(UserStatusResponseDto::from)
            .toList();
    }

    @Override
    public UserStatusResponseDto update(UUID id, UserStatusUpdateDto request) {
        UserStatus userStatus = userStatusRepository.findById(id);
        if (Objects.isNull(userStatus)) {
            throw new RuntimeException("존재하지 않는 유저 상태입니다.");
        }

        userStatus.update(request.lastActiveAt());
        userStatusRepository.save(userStatus);

        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public UserStatusResponseDto updateByUserId(UUID userId, UserStatusUpdateDto request) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 유저 상태입니다."));

        userStatus.update(request.lastActiveAt());
        userStatusRepository.save(userStatus);

        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public void deleteById(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id);
        if (Objects.isNull(userStatus)) {
            throw new RuntimeException("존재하지 않는 읽음 상태입니다");
        }
       userStatusRepository.deleteById(id);
    }
}
