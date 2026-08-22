package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> data;

    @Override
    public UserStatus save(UserStatus userStatus) {
        data.put(userStatus.getUuid(), userStatus);
        return data.get(userStatus.getUuid());
    }

    @Override
    public Optional<UserStatus> read(UUID uuid) {
        return Optional.ofNullable(data.get(uuid));
    }

    @Override
    public void delete(UUID uuid) {
        data.remove(uuid);
    }

    @Override
    public List<UserStatus> readAll() {
        return data.values().stream().toList();
    }


}
