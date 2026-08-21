package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFUserStatusRepository implements UserStatusRepository {

    protected Map<UUID, UserStatus> data;

    @Override
    public void save(UserStatus userStatus) {
        data.put(userStatus.getId(),userStatus);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return data.values().stream()
            .filter(userStatus -> userStatus.getUserId().equals(userId))
            .findFirst();
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return data.values().stream()
            .anyMatch(userStatus -> userStatus.getUserId().equals(userId));
    }

    @Override
    public UserStatus findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<UserStatus> findAll() {
        return data.values().stream().toList();
    }
}
