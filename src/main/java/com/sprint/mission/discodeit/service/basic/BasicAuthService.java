package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.LoginRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BasicAuthService implements AuthService {


    UserRepository userRepository;

    @Override
    public UserDto login(LoginRequest request) {

        User user = userRepository.findByUserName(request.userName())
            .filter(u->u.getPassword().equals(request.password()))
            .orElseThrow(() -> new DiscodeitException(ErrorCode.INVALID_CREDENTIAL));

        return new UserDto(
            user.getId(),
            user.getUserName(),
            user.getEmail(),
            user.getNickName(),
            user.getProfileId(),
            false,
            user.getCreatedAt()
        );
    }
}
