package com.sprint.mission.discodeit.file.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
    void userInit();
    void userCreate(String channelName);
    void userUpdate(String userName, String updateUserName);
    void userDelete(String userName);
    List<User> allPrintUser();
    void printUser(String userName);
}
