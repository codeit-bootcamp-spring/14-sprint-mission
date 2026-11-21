package com.sprint.mission.discodeit.user.domain.service;

import com.sprint.mission.discodeit.user.domain.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {

    void validateUsernameNotDuplicated(String username);
    void validateEmailNotDuplicated(String email);
}
