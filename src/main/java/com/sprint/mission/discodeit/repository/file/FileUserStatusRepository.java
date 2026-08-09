package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserStatusRepository implements UserStatusRepository {
    private final List<UserStatus> userStatusList;

    public FileUserStatusRepository() {
        this.userStatusList = new ArrayList<>();
    }

    @Override
    public void save(UserStatus userStatus) {
        userStatusList.add(userStatus);
        saveFile();
    }

    @Override
    public UserStatus findById(UUID id) {
        for (UserStatus each : userStatusList) {
            if (each.getId().equals(id)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        for (UserStatus each : userStatusList) {
            if (each.getUserId() != null && each.getUserId().equals(userId)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusList;
    }

    @Override
    public void delete(UUID id) {
        UserStatus target = findById(id);
        if (target != null) {
            userStatusList.remove(target);
            saveFile();
        }
    }

    private void saveFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("userstatuslist.ser"))) {
            oos.writeObject(userStatusList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
