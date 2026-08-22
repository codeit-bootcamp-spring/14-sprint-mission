package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;

import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    public BasicAuthService(UserRepository userRepository, UserStatusRepository userStatusRepository) {
        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
    }

    @Override
    public UserDto login(LoginRequest request) {
        // username이 없는 것과 비밀번호가 틀린 것을 구분해서 알려주지 않는다.
        // 구분해서 알려주면 존재하는 계정을 추측하는 데 쓰일 수 있다.
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new DiscodeitException(ExceptionType.INVALID_CREDENTIALS));
        if (!user.getPassword().equals(request.password())) {
            throw new DiscodeitException(ExceptionType.INVALID_CREDENTIALS);
        }

        // 로그인 = 접속. UserStatus는 유저 생성 시 항상 같이 만들어 두므로 없을 일이 없다.
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new DiscodeitException(ExceptionType.USER_STATUS_NOT_FOUND,
                        "유저 상태를 찾을 수 없습니다! userId: " + user.getId()));
        userStatus.updateLastActiveAt(Instant.now());
        userStatusRepository.save(userStatus);

        return new UserDto(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                userStatus.isOnline()
        );
    }
}
