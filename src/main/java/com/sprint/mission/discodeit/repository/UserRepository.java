package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    //메모리 업데이트 반환값이 필요없다고 생각함
    public abstract void save(User user);

    //메모리에서 id로 읽어오기
    public abstract User findById(UUID id);

    //이메일과 이름은 중복되면 안되서 이걸로 검증하려고 넣음
    User findByEmail(String email);

    User findByName(String name);

    //유저 리스트 가져오기
    public abstract List<User> findAll();

    //메모리에서 유저 지우기
    public abstract void delete(UUID id);

}
