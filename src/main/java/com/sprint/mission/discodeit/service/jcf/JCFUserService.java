package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new LinkedHashMap<>();
    }

    @Override
    public User create(String UserName, String email) {
        User createUser = new User(UserName, email);
        data.put(createUser.getId(), createUser);
        return data.get(createUser.getId());
    }

    @Override
    public User findById(UUID id) {
        User user = data.get(id);
        if (user == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다! Id: " + id);
        }

        return data.get(id);

    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID id, String username, String email) {
        User updateUser = findById(id);
        updateUser.update(username, email);
        return updateUser;
    }

    @Override
    public void delete(UUID id) {

        data.remove(id);
    }
}
