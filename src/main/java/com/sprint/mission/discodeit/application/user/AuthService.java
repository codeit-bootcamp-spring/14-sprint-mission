package com.sprint.mission.discodeit.application.user;

import com.sprint.mission.discodeit.adapter.in.controller.user.dto.UserDto;
import com.sprint.mission.discodeit.config.security.CustomUserDetails;
import com.sprint.mission.discodeit.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SessionRegistry sessionRegistry;

    public void removeSession(User user) {
        CustomUserDetails principal = new CustomUserDetails(UserDto.from(user), user.getPassword());
        sessionRegistry.getAllSessions(principal, false).forEach(SessionInformation::expireNow);
    }
}
