package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.User;

public interface UserService extends Service<User> {


    void update(User user, String updatedname, String updatedemail);


}
