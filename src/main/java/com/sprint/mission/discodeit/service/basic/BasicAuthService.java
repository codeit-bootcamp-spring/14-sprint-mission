package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.authdto.AuthCreateRequestDto;
import com.sprint.mission.discodeit.dto.userdto.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository; // 접속 상태 조회를 위해 추가


    @Override
    public UserResponseDto login(AuthCreateRequestDto authCreateRequestDto) {
        User user = userRepository.findByName(authCreateRequestDto.getName());
        if (Objects.isNull(user)) {
            throw new RuntimeException("올바른 이름을 입력해주세요.");
        }
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId());
        return UserResponseDto.from(user, userStatus);
    }
}
