package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.LoginRequestDto;
import com.sprint.mission.discodeit.dto.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    public UserResponseDto login(LoginRequestDto loginrequest) {
        User user = userRepository.findByName(loginrequest.username())
            .orElseThrow(() ->new RuntimeException("존재하지 않는 유저 입니다"));

        if (!user.getPassword().equals(loginrequest.password())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다;");
        }
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
            .orElseThrow(() ->new RuntimeException("존재하지 않는 유저 상태 입니다"));
        boolean online = userStatus.isOnline();
        return UserResponseDto.from(user, online);


    }

}
