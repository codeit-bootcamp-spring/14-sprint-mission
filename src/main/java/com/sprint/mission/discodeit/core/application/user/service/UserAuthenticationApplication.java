package com.sprint.mission.discodeit.core.application.user.service;

import com.sprint.mission.discodeit.core.application.user.port.in.UserAuthenticationUseCase;
import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserAuthenticationResult;
import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserResult;
import com.sprint.mission.discodeit.core.application.user.port.out.UserPort;
import com.sprint.mission.discodeit.core.application.user.port.out.UserSessionPort;
import com.sprint.mission.discodeit.core.common.annotation.ApplicationService;
import com.sprint.mission.discodeit.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class UserAuthenticationApplication implements UserAuthenticationUseCase {
    private final UserPort users;
    private final UserSessionPort userSessions;

    @Override
    @Transactional(readOnly = true)
    public UserAuthenticationResult getUserForAuthentication(String username) {
        User foundUser = users.get(username);
        boolean isOnline = userSessions.isOnline(foundUser.getId());
        return new UserAuthenticationResult(UserResult.from(foundUser, isOnline), foundUser.getPassword());
    }
}
