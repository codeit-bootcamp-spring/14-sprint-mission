package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.auth.dto.LoginRequestDto;
import com.sprint.mission.discodeit.global.exception.InvalidCredentialsException;
import com.sprint.mission.discodeit.global.exception.NotFoundException;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    public UserResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUserName(loginRequestDto.name())
            .orElseThrow(InvalidCredentialsException::new);

        if (!user.getPassword().equals(loginRequestDto.password())) {
            throw new InvalidCredentialsException();
        }

        UserStatus userStatus = userStatusRepository.findByUserId(user.getUserId())
            .orElseThrow(() -> NotFoundException.userStatusByUser(user.getUserId()));

        userStatus.userLogin();
        userStatusRepository.update(userStatus);

        return UserResponseDto.from(user, userStatus);
    }
}
