package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class FileUserRepository implements UserRepository {
    private final static List<User> users = new ArrayList<>();

    public FileUserRepository() {
        userLoad();
    }

    public void userLoad() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("data/User.ser"))){
            users.addAll((List<User>) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("기존 유저 데이터가 없습니다.");
        }
    }

    public void userFlush() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("data/User.ser"))) {
            objectOutputStream.writeObject(this.users);
        } catch (IOException e) {
            System.out.println("저장할 유저가 없습니다.");
        }
    }

    public List<String> readUserName() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("data/User.txt"))) {
            String name;
            List<String> userName = new ArrayList<>();
            while ((name = bufferedReader.readLine()) != null) {
                userName.add(name);
            }
            return userName;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User userAdd(User user) {
        this.users.add(user);
        return user;
    }

    @Override
    public Optional<User> findByUser(String userName) {
        return users.stream()
                .filter(user -> userName.equals(user.getUserName()))
                .findFirst();
    }

    @Override
    public void delete(User user) {
        users.remove(user);
    }

    @Override
    public List<User> findAllUser() {
        return new ArrayList<>(users);
    }
}
