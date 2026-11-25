package com.sprint.mission.discodeit.adapter.out.persistence.repository.user;

import com.sprint.mission.discodeit.core.application.user.port.out.UserPort;
import com.sprint.mission.discodeit.core.common.persistence.NotFoundException;
import com.sprint.mission.discodeit.core.domain.user.Role;
import com.sprint.mission.discodeit.core.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserAdapter implements UserPort {
    private final UserJpaRepository repository;

    @Override
    public boolean existsByUsernameOrEmail(String username, String email) {
        return repository.existsByUsernameOrEmail(username, email);
    }

    @Override
    public User get(String username) {
        String errorMessage = String.format("User by username %s not found", username);
        return repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(errorMessage));
    }

    @Override
    public boolean existsByUsernameAndIdNot(String username, UUID id) {
        return repository.existsByUsernameAndIdNot(username, id);
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, UUID id) {
        return repository.existsByEmailAndIdNot(email, id);
    }

    @Override
    public boolean existsByRole(Role role) {
        return repository.existsByRole(role);
    }

    @Override
    public List<User> findByIdIn(List<UUID> userIds) {
        return repository.findByIdIn(userIds);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(User user) {
        repository.delete(user);
    }
}
