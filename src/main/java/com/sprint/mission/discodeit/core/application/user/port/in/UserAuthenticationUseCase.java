package com.sprint.mission.discodeit.core.application.user.port.in;

import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserAuthenticationResult;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

public interface UserAuthenticationUseCase {
    UserAuthenticationResult getUserForAuthentication(@NotNull String username);
}
