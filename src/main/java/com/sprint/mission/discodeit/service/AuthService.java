package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import org.springframework.security.access.prepost.PreAuthorize;

public interface AuthService {

    UserDto initAdmin();

    @PreAuthorize("hasRole('ADMIN')")
    UserDto updateRole(RoleUpdateRequest request);
}
