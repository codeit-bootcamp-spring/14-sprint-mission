package com.sprint.mission.discodeit.auth.application.basic;

import com.sprint.mission.discodeit.auth.dto.AuthLoginRequestDto;
import com.sprint.mission.discodeit.auth.dto.AuthLoginResponseDto;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.common.exception.AuthenticationFailedException;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.user.domain.UserStatus;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.auth.application.AuthService;
import com.sprint.mission.discodeit.user.repository.UserStatusRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public AuthLoginResponseDto login(AuthLoginRequestDto authLoginRequestDto) {

        User user = userRepository.findByUsername(authLoginRequestDto.username()).orElseThrow(NoSuchElementException::new);

        if(user.checkPassword(authLoginRequestDto.password())){
            UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                    .orElseThrow(NoSuchElementException::new);

            userStatus.updateLastAccessAt();
            userStatusRepository.update(userStatus);
            return AuthLoginResponseDto.from(user.getId(), user.getCreatedAt(), user.getUpdatedAt(),
                                             user.getUserName(), user.getEmail(),user.getPassword(), user.getProfileId());
        }

        throw new AuthenticationFailedException();

    }
}
