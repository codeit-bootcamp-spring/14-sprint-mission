package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserRepository implements UserRepository {


    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public Optional<User> findById(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public List<User> readAll() {
        return List.of();
    }
}
