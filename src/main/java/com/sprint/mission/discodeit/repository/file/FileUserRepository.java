package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
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
    public boolean existsByNameOrEmail(String name, String email) {
        return buffer.values().stream()
                .anyMatch(user -> containsNameOrEmail(name, email, user));
    }

    private static boolean containsNameOrEmail(String name, String email, User user) {
        return user.getName().equals(name) || user.getEmail().equals(email);
    }

    @Override
    public void update(UUID id, String name, String email, String password, @Nullable UUID profileId) {
        findById(id).ifPresent(user -> user.update(name, email, password, profileId));
    }


}
