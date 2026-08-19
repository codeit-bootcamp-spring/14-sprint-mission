package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    public UserStatus create(UserStatusCreateDto dto) {
        UUID userId = dto.getUserId();
        if(!userRepository.existsById(userId)) {
            throw new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE);
        }
        if(userStatusRepository.existsByUserId(userId)) {
            throw new CustomException(ExceptionType.USERSTATUS_ALREADY_EXISTS);
        }

        return userStatusRepository.create(dto.toUserStatus());
    }

    public UserStatus getUserStatus(UUID id) {
        return userStatusRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USERSTATUS_NOT_FOUND_IN_DATABASE));
    }

    public List<UserStatus> getAllUserStatus() {
        return userStatusRepository.findAll();
    }

    public void update(UUID id) {
        userStatusRepository.update(id);
    }

    public void updateByUserId(UUID userId) {
        userStatusRepository.updateByUserId(userId);
    }

    public void delete(UUID id) {
        userStatusRepository.deleteById(id);
    }

}
