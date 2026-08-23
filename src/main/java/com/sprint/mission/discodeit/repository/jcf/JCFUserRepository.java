package com.sprint.mission.discodeit.repository.jcf;

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
        havingValue = "jcf"
)
public class JCFUserRepository extends AbstractJCFRepository<User>
        implements UserRepository {

    @Override
    public boolean existsByName(String name) {
        return super.STORE.values().stream()
                .anyMatch(user -> user.getName().equals(name));
    }

    @Override
    public boolean existsAllByIds(List<UUID> ids) {
        return ids.stream()
                .allMatch(id -> existsById(id));
    }

    @Override
    public boolean existsByNameOrEmail(String name, String email) {
        return findAll().stream()
                .anyMatch(user -> user.getName().equals(name)
                        || user.getEmail().equals(email));
    }

    @Override
    public Optional<User> findByNameAndPassword(String name, String password) {
        return  findAll().stream()
                .filter(user -> user.getName().equals(name)
                        && user.getPassword().equals(password))
                .findFirst();
    }

    @Override
    public User update(UUID id, String name, String email, String password, @Nullable UUID profileId) {
        User updating = findById(id).orElseThrow();
        return updating.update(name, email, password, profileId);
    }

}
