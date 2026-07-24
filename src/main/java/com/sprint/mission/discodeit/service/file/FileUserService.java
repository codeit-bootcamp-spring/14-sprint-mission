package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class FileUserService extends FileService<User> implements UserService {

    public FileUserService(){
        super("users.ser");
    }

    @Override
    public void update(User user, String updatedname, String updatedemail) {
        Map<UUID, User> data = loadData();
        User existing = data.get(user.getId());
        if(Objects.isNull(existing)){
            throw new RuntimeException("존재하지 않는 유저 입니다");
        }
        existing.setName(updatedname);
        existing.setEmail(updatedemail);
        saveData(data);

    }
}
