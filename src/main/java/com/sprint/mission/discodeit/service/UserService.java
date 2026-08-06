package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface UserService {

    //Crate
    public User createUser(String email, String name);

    //Read (id를 받아 User반환)
    public User readUser(UUID id);

    //Read (전체 조회)
    public List<User> readAllUser();

    //Update (유저 정보 업데이트)
    public void updateUser(UUID id, String name);

    //Delete (유저 정보 삭제)
    public void deleteUser(UUID id) ;

}
