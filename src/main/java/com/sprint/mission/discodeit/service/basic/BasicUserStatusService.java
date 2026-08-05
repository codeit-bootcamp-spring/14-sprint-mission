package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatus create(UserStatusCreateDto dto) {
        UUID userId = dto.getUserId();
        if(!userRepository.existsById(userId)) {
            throw new RuntimeException("유저 없음");
        }
        if(userStatusRepository.existsByUserId(userId)) {
            throw new RuntimeException("UserStatus 이미 존재");
        }

        return userStatusRepository.create(dto.toUserStatus());
    }

    @Override
    public UserStatus getUserStatus(UUID id) {
        return userStatusRepository.findById(id).orElseThrow();
    }

    @Override
    public List<UserStatus> getAllUserStatus() {
        return userStatusRepository.findAll();
    }

    @Override
    public void update(UUID id) {
        userStatusRepository.update(id);
    }

    @Override
    public void updateByUserId(UUID userId) {
        userStatusRepository.updateByUserId(userId);
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.deleteById(id);
    }

}
