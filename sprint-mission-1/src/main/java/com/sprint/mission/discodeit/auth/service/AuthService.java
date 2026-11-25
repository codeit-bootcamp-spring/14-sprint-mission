package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.auth.dto.UserRoleUpdateRequestDto;
import com.sprint.mission.discodeit.auth.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ExceptionType;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.mapper.UserMapper;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SessionRegistry sessionRegistry;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto updateRole(UserRoleUpdateRequestDto request) {
        User user = userRepository.findById(request.userId())
            .orElseThrow(() -> new DiscodeitException(ExceptionType.USER_NOT_FOUND));
        user.updateRole(request.newRole());
        expireSessions(user.getId());
        return userMapper.toDto(user);
    }

    // 세션의 권한은 로그인 시점의 사본이라 바로 바뀌지 않는다.
    // 만료 표시만 하고, 실제 차단은 다음 요청 때 ConcurrentSessionFilter가 한다
    private void expireSessions(UUID userId) {
        sessionRegistry.getAllPrincipals().stream()
            .filter(principal -> principal instanceof DiscodeitUserDetails details
                && details.getUserDto().id().equals(userId))
            .flatMap(principal -> sessionRegistry.getAllSessions(principal, false).stream())
            .forEach(SessionInformation::expireNow);
        log.info("권한 변경으로 세션 만료: userId={}", userId);
    }
}
