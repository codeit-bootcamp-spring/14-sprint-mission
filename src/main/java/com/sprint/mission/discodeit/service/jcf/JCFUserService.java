package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    // JCF(Map)를 활용하여 데이터를 저장할 수 있는 필드(data)를 final로 선언
    private final Map<UUID, User> data;

    // 생성자에서 초기화
    public JCFUserService() {
        this.data = new HashMap<>();
    }

    // data 필드를 활용해 생성, 조회, 수정, 삭제하는 메소드 구현
    // 생성
    @Override
    public User createUser(String nickName, String email) {
        User user = new User(nickName, email);
        data.put(user.getId(), user);
        return user;
    }

    // 상세 조회
    @Override
    public User readUser(UUID id) {
        User user = data.get(id);
        if (user == null) {
            throw new IllegalArgumentException("해당 ID는 존재하지 않습니다.");
        }
        return user;
    }

    // 전체 조회
    @Override
    public List<User> readAllUsers() {
        return new ArrayList<>(data.values());
    }

    // 수정
    @Override
    public User updateUser(UUID id, String nickName, String email) {
        User user = data.get(id);
        if (user != null) {
            user.update(nickName, email);
        }
        return user;
    }

    // 삭제
    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }
}
