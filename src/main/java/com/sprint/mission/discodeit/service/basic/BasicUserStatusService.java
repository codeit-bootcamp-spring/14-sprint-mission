package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.userstatus.UserStatus;
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

    public UserStatus create(UUID userId) {
        if(!userRepository.existsById(userId)) {
            throw new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE);
        }
        if(userStatusRepository.existsByUserId(userId)) {
            throw new CustomException(ExceptionType.USERSTATUS_ALREADY_EXISTS);
        }

        UserStatus userStatus = new UserStatus(userId);
        return userStatusRepository.create(userStatus);
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
