package com.sprint.mission.discodeit.auth.application.basic;

import com.sprint.mission.discodeit.auth.application.AuthService;
import com.sprint.mission.discodeit.auth.dto.AuthLoginRequestDto;
import com.sprint.mission.discodeit.common.exception.AuthenticationFailedException;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto login(AuthLoginRequestDto authLoginRequestDto) {
        String username = authLoginRequestDto.username();

        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> {
                    log.warn("로그인 실패 - 존재하지 않는 사용자: username = {}", username);
                    return new NoSuchElementException();
                });

        if (user.checkPassword(authLoginRequestDto.password())) {
            UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                    .orElseThrow(NoSuchElementException::new);

            userStatus.updateLastAccessAt();
//            userStatusRepository.update(userStatus); // 변경 감지

            log.info("로그인 완료: username = {}", user.getUserName());
            return userMapper.toDto(user);
        }

        log.warn("로그인 실패 - 비밀번호 불일치: username = {}", user.getUserName());
        throw new AuthenticationFailedException();

    }
}
