package com.sprint.mission.discodeit.auth.adapter.security;

import com.sprint.mission.discodeit.user.application.dto.UserRoleChanged;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserRoleChangedSessionListener {

  private final SessionRegistry sessionRegistry;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onRoleChanged(UserRoleChanged event) {
    for (Object principal : sessionRegistry.getAllPrincipals()) {
      if (principal instanceof CustomUserDetails user &&
          event.userId().equals(user.getUserId())) {
        sessionRegistry.getAllSessions(user, false)
            .forEach(SessionInformation::expireNow);
      }
    }
  }

}
