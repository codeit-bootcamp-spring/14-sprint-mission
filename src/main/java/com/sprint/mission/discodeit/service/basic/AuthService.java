package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.InvalidCredentialsException;
import com.sprint.mission.discodeit.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    public UserResponseDto login(LoginRequestDto loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username())
            .orElseThrow(() -> new InvalidCredentialsException(loginRequest.username()));

        if (!user.getPassword().equals(loginRequest.password())) {
            throw new InvalidCredentialsException(loginRequest.username());
        }

        UserStatus userStatus = userStatusRepository.findByUser_Id(user.getId())
            .orElseThrow(() -> UserStatusNotFoundException.byUserId(user.getId()));

        boolean online = userStatus.isOnline();
        return UserResponseDto.from(user, online);
    }

}
