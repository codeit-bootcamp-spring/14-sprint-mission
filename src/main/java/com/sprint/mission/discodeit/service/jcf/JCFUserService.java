package com.sprint.mission.discodeit.service.jcf;

import static com.sprint.mission.discodeit.service.basic.BasicUserService.ERROR_USER_NOT_FOUND;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User create(String name, String email, String nickname) {
        User user = User.create(name, email, nickname);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, String name, String email, String nickname) {
        User user = Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND + id));
            user.changeName(name);
            user.changeEmail(email);
            user.changeNickname(nickname);
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
