package com.sprint.mission.discodeit.auth.application.basic;

import com.sprint.mission.discodeit.auth.dto.AuthLoginRequestDto;
import com.sprint.mission.discodeit.auth.dto.AuthLoginResponseDto;
import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.common.exception.AuthenticationFailedException;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import com.sprint.mission.discodeit.auth.application.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;

    @Override
    public AuthLoginResponseDto login(AuthLoginRequestDto authLoginRequestDto) {

        User user = userRepository.findByUsername(authLoginRequestDto.username()).orElseThrow(NoSuchElementException::new);
        if(user.checkPassword(authLoginRequestDto.password())){
            return AuthLoginResponseDto.from(user.getUserName(), user.getEmail());
        }

        throw new AuthenticationFailedException();

    }
}
