package com.sprint.mission.discodeit.service.file;

import static com.sprint.mission.discodeit.service.basic.BasicUserService.ERROR_USER_NOT_FOUND;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileUserService implements UserService {

    private static final String FILE_PATH = "users.ser";

    private Map<UUID, User> loadData(){
        File file = new File(FILE_PATH);
        if (!file.exists()){
            return new HashMap<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveData(Map<UUID, User> data){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User create(String name, String email, String nickname) {
        Map<UUID, User> data = loadData();
        User user = User.create(name, email, nickname);
        data.put(user.getId(), user);
        saveData(data);
        return user;
    }

    @Override
    public Optional<User> read(UUID id) {
        Map<UUID, User> data = loadData();
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> readAll() {
        Map<UUID, User> data = loadData();
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, String name, String email, String nickname) {
        Map<UUID, User> data = loadData();
        User user = Optional.ofNullable(data.get(id))
                        .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND + id));

            user.changeName(name);
            user.changeEmail(email);
            user.changeNickname(nickname);
            saveData(data);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, User> data = loadData();
        data.remove(id);
        saveData(data);
    }
}
