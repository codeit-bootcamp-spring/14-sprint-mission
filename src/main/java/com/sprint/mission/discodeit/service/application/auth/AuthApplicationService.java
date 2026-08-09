package com.sprint.mission.discodeit.service.application.auth;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.auth.LoginRequestDto;
import com.sprint.mission.discodeit.dto.auth.LoginResponseDto;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthApplicationService {
    private final UserRepository userRepository;

    public AuthApplicationService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }


    public LoginResponseDto login(LoginRequestDto loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())    // username은 고유하다
                .orElseThrow(() -> new CustomException(ExceptionType.LOGIN_FAILED));

        if (!user.matchesPassword(loginRequest.getPassword())) {
            log.error("로그인 실패. username={}", loginRequest.getUsername());
            throw new CustomException(ExceptionType.LOGIN_FAILED);
        }

        log.info("로그인 완료: username={}", user.getUsername());

        return LoginResponseDto.from(user);
    }
}
