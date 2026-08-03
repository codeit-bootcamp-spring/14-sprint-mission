package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.Files;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileUserRepository extends MapFileIO<User>
        implements UserRepository {

    private final Map<UUID, User> EMPTY_BUFFER = new HashMap<>();
    private Map<UUID, User> buffer;

    public FileUserRepository() {
        super(Files.USER);
        this.buffer = Optional.of(file)
                .filter(file -> file.exists() && file.length() != 0)
                .map(file -> super.readFile())
                .orElseGet(() -> super.writeFile(EMPTY_BUFFER));
    }


    @Override
    public User create(User user) {
        UUID userId = user.getId();
        return findById(userId).orElseGet(() -> {
            buffer.put(userId, user);
            super.writeFile(buffer);
            return user;
        });
    }

    @Override
    public boolean existsById(UUID id) {
        return findById(id).isPresent();
    }

    @Override
    public boolean existsByNameOrEmail(String name, String email) {
        buffer = super.readFile();
        return buffer.values().stream()
                .anyMatch(user -> containsNameOrEmail(name, email, user));
    }

    private static boolean containsNameOrEmail(String name, String email, User user) {
        return user.getName().equals(name) || user.getEmail().equals(email);
    }

    @Override
    public Optional<User> findById(UUID id) {
        buffer = super.readFile();
        return Optional.ofNullable(buffer.get(id));
    }

    @Override
    public List<User> findAll() {
        buffer = readFile();
        return new ArrayList<>(buffer.values());
    }

    @Override
    public void updateName(UUID id, String name) {
        findById(id).ifPresent(retrieved -> {
            retrieved.updateName(name);
            writeFile();
        });
    }

    @Override
    public void deleteById(UUID id) {
        findById(id).ifPresent(retrieved -> {
            buffer.remove(id);
            writeFile();
        });
    }

    private void writeFile() {
        super.writeFile(buffer);
    }

}
