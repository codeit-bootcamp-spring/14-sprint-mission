package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.auth.dto.UserRoleUpdateRequestDto;
import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ExceptionType;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDto updateRole(@Valid UserRoleUpdateRequestDto request) {
        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new DiscodeitException(ExceptionType.USER_NOT_FOUND));
        user.updateRole(request.newRole());
        return userMapper.toDto(user);
    }
}
