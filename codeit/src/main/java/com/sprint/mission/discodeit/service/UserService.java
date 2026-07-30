package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserService {

    default void create(User user){
        throw new RuntimeException("이 메세지는 인터페이스 create 내 디폴트 메세지입니다.");
    }
    default User read(User user){
        throw new RuntimeException("이 메세지는 인터페이스 read 내 디폴트 메세지입니다.");
    }
    default void update(User user,String name, String password, String email){
        throw new RuntimeException("이 메세지는 인터페이스 update 내 디폴트 메세지입니다.");
    }
    default void delete(User user){
        throw new RuntimeException("이 메세지는 인터페이스 delete 내 디폴트 메세지입니다.");
    }
}
