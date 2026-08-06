package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.auth.dto.LoginRequestDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import com.sprint.mission.discodeit.userstatus.repository.UserStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    public AuthServiceImpl(UserRepository userRepository,
        UserStatusRepository userStatusRepository) {
        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
    }

    public UserResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUserName(loginRequestDto.name())
            .orElseThrow(
                () -> new IllegalArgumentException("일치하는 유저가 없습니다: " + loginRequestDto.name()));

        if (!user.getPassword().equals(loginRequestDto.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        UserStatus userStatus = userStatusRepository.findByUserId(user.getUserId())
            .orElseThrow(
                () -> new IllegalArgumentException("저장된 유저의 상태가 없습니다: " + user.getUserId()));

        userStatus.userLogin();
        userStatusRepository.update(userStatus);

        return UserResponseDto.from(user, userStatus);
    }
}
