package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository
        extends AbstractFileRepository<User>
        implements UserRepository {

    private static final String USER_FILENAME = "users.ser";

    private final Map<UUID, User> userMap;

    public FileUserRepository(
            @Value("${discodeit.repository.file-directory:.discodeit/objects}")
            String fileDirectory
    ) {
        super("User", fileDirectory, USER_FILENAME);
        this.userMap = loadFile();
    }

    @Override
    public User save(User user) {
        User previousUser = userMap.put(user.getId(), user);
        try {
            saveFile(userMap);
        } catch (RuntimeException exception) {
            if (Objects.isNull(previousUser)) {
                userMap.remove(user.getId());
            } else {
                userMap.put(previousUser.getId(), previousUser);
            }
            throw exception;
        }
        return user;
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userMap.values().stream()
                .filter(user -> Objects.equals(user.getUsername(), username))
                .findFirst();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userMap.values().stream()
                .anyMatch(user -> Objects.equals(user.getUsername(), username));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userMap.values().stream()
                .anyMatch(user -> Objects.equals(user.getEmail(), email));
    }

    @Override
    public List<User> findAll() {
        return userMap.values().stream().toList();
    }

    @Override
    public void delete(UUID userId) {
        User deletedUser = userMap.remove(userId);
        try {
            saveFile(userMap);
        } catch (RuntimeException exception) {
            if (Objects.nonNull(deletedUser)) {
                userMap.put(deletedUser.getId(), deletedUser);
            }
            throw exception;
        }
    }

}
