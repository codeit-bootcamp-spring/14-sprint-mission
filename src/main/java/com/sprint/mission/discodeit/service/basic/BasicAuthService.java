package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.dto.user.UserLoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserResponseDto login(@Valid UserLoginRequestDto dto) {
        User retrieved = userRepository.findByNameAndPassword(dto.getName(), dto.getPassword())
                .orElseThrow(() -> new NoSuchElementException(String.format("잘못된 name 혹은 password")));
        UserStatus userStatus = userStatusRepository.findByUserId(retrieved.getId())
                .orElseThrow(() -> new NoSuchElementException(String.format("잘못된 userId")));
        userStatus.updateLastSeenAt();
        return UserResponseDto.of(retrieved, userStatus);
    }
}
