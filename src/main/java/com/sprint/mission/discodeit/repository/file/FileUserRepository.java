package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file"
)
public class FileUserRepository extends AbstractFileRepository<User>
        implements UserRepository {

    public FileUserRepository() {
        super(Files.USER);
    }

    @Override
    public boolean existsById(UUID id) {
        return findById(id).isPresent();
    }

    @Override
    public boolean existsAllByIds(List<UUID> ids) {
        return ids.stream()
                .allMatch(id -> existsById(id));
    }

    @Override
    public boolean existsByNameOrEmail(String name, String email) {
        return buffer.values().stream()
                .anyMatch(user -> containsNameOrEmail(name, email, user));
    }

    private boolean containsNameOrEmail(String name, String email, User user) {
        return user.getName().equals(name) || user.getEmail().equals(email);
    }

    @Override
    public Optional<User> findByNameAndPassword(String name, String password) {
        return super.buffer.values().stream()
                .filter(user -> user.getName().equals(name) && user.getPassword().equals(password))
                .findFirst();
    }

    @Override
    public void update(UUID id, String name, String email, String password, @Nullable UUID profileId) {
        findById(id).ifPresent(user -> user.update(name, email, password, profileId));
        super.writeFromBufferToFile();
    }


}
