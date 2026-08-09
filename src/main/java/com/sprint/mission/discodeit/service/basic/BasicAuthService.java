package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.authLogin.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.authLogin.AuthLoginResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.AuthenticationFailedException;
import com.sprint.mission.discodeit.exception.NoSuchElementException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
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
