package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.NoSuchElementException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BasicAuthService implements AuthService {
    // 순환 참소 방지를 위해 UserService가 아닌 UserRepository를 주입받음.
    UserRepository userRepository;

    @Override
    public User login(LoginRequest request) {
        return userRepository.findByUsernameAndPassword(request.username(), request.password())
                .orElseThrow(() -> new NoSuchElementException("아이디 또는 비번이 일치하지 않습니다."));
    }
}
