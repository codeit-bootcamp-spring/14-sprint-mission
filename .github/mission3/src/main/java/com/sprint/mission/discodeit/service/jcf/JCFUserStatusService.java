package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusRequest;
import com.sprint.mission.discodeit.dto.userStatusDto.UserStatusResponse;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JCFUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UserStatusResponse create(UserStatusRequest request) {
        return null;
    }

    @Override
    public UserStatusResponse findUserStatus(UserStatusRequest request) {
        return null;
    }

    @Override
    public List<UserStatusResponse> findAll() {
        return List.of();
    }

    @Override
    public void delete(UserStatusRequest request) {

    }

    @Override
    public void updateByUserId(UUID userId) {

    }
}
