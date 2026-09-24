package com.sprint.mission.discodeit.auth.application.basic;

import com.sprint.mission.discodeit.auth.dto.AuthLoginRequestDto;
import com.sprint.mission.discodeit.auth.dto.AuthLoginResponseDto;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.common.exception.AuthenticationFailedException;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.dto.UserResponseDto;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.auth.application.AuthService;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        User user = userRepository.findByUserName(authLoginRequestDto.username()).orElseThrow(NoSuchElementException::new);

        if(user.checkPassword(authLoginRequestDto.password())){
            UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                    .orElseThrow(NoSuchElementException::new);

            userStatus.updateLastAccessAt();
//            userStatusRepository.update(userStatus); // 변경 감지
            return userMapper.toDto(user);
        }

        throw new AuthenticationFailedException();

    }
}
