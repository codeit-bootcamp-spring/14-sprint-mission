package com.sprint.mission.discodeit.repository.file;

import org.springframework.stereotype.Repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FileUserRepository implements UserRepository {
    final List<User> userList;

    public FileUserRepository() {
        this.userList = new ArrayList<>();
    }


    @Override
    public void save(User user) {
        userList.add(user);
        saveUser();
    }

    @Override
    public User findById(UUID id) {
        for (User each : userList) {
            if (each.getId().equals(id)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return this.userList;
    }

    @Override
    public void delete(UUID id) {
        User target = findById(id);
        userList.remove(target);
        saveUser();
    }

    private void saveUser() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("newUserlist.ser"))) {
            objectOutputStream.writeObject(userList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findByEmail(String email) {
        User target = userList.stream()
                .filter(each -> each.getEmail().equals(email))
                .findFirst()
                .orElse(null);
        return target;
    }

    @Override
    public User findByName(String name){
        User target = userList.stream()
                .filter(each -> each.getName().equals(name))
                .findFirst()
                .orElse(null);
        return target;
    };
}
