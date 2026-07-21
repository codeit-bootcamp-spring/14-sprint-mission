package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
    User userCreate(String userName);
    void userUpdate(String userName, String updateUserName);
    void userDelete(String userName);
    List<User> allPrintUser();
    void printUser(String userName);
}
