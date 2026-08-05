package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.entity.UserStatus;
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
            throw new RuntimeException("유저 없음");
        }
        if(userStatusRepository.existsByUserId(userId)) {
            throw new RuntimeException("UsetStatus 이미 존재");
        }

        return userStatusRepository.create(dto.toUserStatus());
    }

    public UserStatus getUserStatus(UUID id) {
        return userStatusRepository.findById(id).orElseThrow();
    }

    public List<UserStatus> getAllUserStatus() {
        return userStatusRepository.findAll();
    }




}
