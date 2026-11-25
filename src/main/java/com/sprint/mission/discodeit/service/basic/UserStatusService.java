package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusAlreadyExistException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.IService.IUserStatusService;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserStatusService implements IUserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusResponseDto create(UserStatusCreateRequestDto request) {
        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new UserNotFoundException(request.userId()));

        if (userStatusRepository.existsByUser_Id(request.userId())) {
            throw new UserStatusAlreadyExistException(request.userId());
        }

        UserStatus userStatus = request.toEntity(user);
        userStatusRepository.save(userStatus);

        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public UserStatusResponseDto find(UUID id) {
        UserStatus userStatus = userStatusRepository.findById(id)
            .orElseThrow(() -> UserStatusNotFoundException.byId(id));
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserStatusResponseDto> findAll() {
        return userStatusRepository.findAll().stream()
            .map(UserStatusResponseDto::from)
            .toList();
    }

    @Override
    public UserStatusResponseDto update(UUID id, UserStatusUpdateDto request) {
        UserStatus userStatus = userStatusRepository.findById(id)
            .orElseThrow(() -> UserStatusNotFoundException.byId(id));

        userStatus.update(request.newLastActiveAt());


        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public UserStatusResponseDto updateByUserId(UUID userId, UserStatusUpdateDto request) {
        UserStatus userStatus = userStatusRepository.findByUser_Id(userId)
            .orElseThrow(() -> UserStatusNotFoundException.byUserId(userId));

        userStatus.update(request.newLastActiveAt());


        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public void deleteById(UUID id) {
        if (!userStatusRepository.existsById(id)) {
            throw UserStatusNotFoundException.byId(id);
        }

        userStatusRepository.deleteById(id);
    }
}
