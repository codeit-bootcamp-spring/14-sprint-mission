package com.sprint.misson.discodeit.repository.jcf;

import com.sprint.misson.discodeit.entity.User;
import com.sprint.misson.discodeit.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;

public class JCFUserRepository implements UserRepository {

    private final Map<String, User> users = new HashMap<>();

    @Override
    public User create(String name, String email) {
        if (users.containsKey(email)){
            throw new RuntimeException("이미 존재하는 메일 주소입니다.");
        }
        User user = new User(name, email);
        users.put(email, user);
        return users.get(user.getEmail());
    }

    @Override
    public User read(String email) {
        if (!users.containsKey(email)){
            throw new RuntimeException("존재하지 않는 메일 주소입니다.");
        }
        return users.get(email);
    }

    @Override
    public void update(User user, String updateName) {
        if (!users.containsKey(user.getEmail())){
            throw new RuntimeException("존재하지 않는 메일 주소입니다.");
        }
        user.update(updateName);
        System.out.println("업데이트 완료. 계정 정보 = " + user);
    }

    @Override
    public void delete(String email) {
        if (!users.containsKey(email)){
            throw new RuntimeException("존재하지 않는 메일 주소입니다.");
        }
        users.remove(email);
        System.out.println("계정 삭제 완료.");
    }

    @Override
    public void readAll() {
        users.values().stream()
            .forEach(user -> System.out.println(user));
    }
}