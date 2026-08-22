package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userDto.UserRequest;
import com.sprint.mission.discodeit.dto.userDto.UserResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    public UserResponse login(UserRequest userRequest) {
        String name = userRequest.name();
        String password = userRequest.password();

        User user = userRepository.readAll().stream()
            .filter(m ->m.getName().equals(name) && m.getPassword().equals(password))
            .findAny().orElseThrow(() -> new NoSuchElementException("이름과 비밀번호를 다시 확인해주세요."));
        return UserResponse.from(user, null);
    }
}
