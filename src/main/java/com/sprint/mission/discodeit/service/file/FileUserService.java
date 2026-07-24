package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    final List<User> userList;

    public FileUserService() {
        this.userList = new ArrayList<>();
    }

    @Override


//
//    public User createUser(String email, String name) {
//        User user = new User(email, name);
//        userList.add(user);
//        return user;
//    }
    //Create
    public User createUser(String email, String name) {
        User user = new User(email, name);
        userList.add(user);
        //파일안에다가 저장
        //어짜피피 리스트를 통째로 저장하기때문에
        saveUserFile();
        return user;

}

    @Override
    //Read (id를 받아 User반환)
    public User readUser(UUID id) {
        for (User each : userList) {
            if (each.getId().equals(id)) {
                return each;
            }
        }
        return null;
    }

    @Override
    //Read (전체 조회)
    public List<User> readAllUser() {
        return this.userList;
    }

    @Override
    //Update
    public void updateUser(UUID id, String name) {
        User user = readUser(id);
        user.setName(name);
        user.setUpdatedAt();
//      return user;
        //얘도 그냥 객체 하나하나 업데이트가 아닌 그냥 메모리에있는 수정된 리스트를 통째로 업데이트하면됌
        saveUserFile();
    }

    @Override
    //Delete
    public void deleteUser(UUID id) {
        User user = readUser(id);
        userList.remove(user);
        //이 친구 또한 그냥 객체 하나하나 업데이트가 아닌 그냥 메모리에있는 수정된 리스트를 통째로 업데이트하면됌
        saveUserFile();
    }

    // CRUD내부에 들어간 영속화 로직을 그냥 메서드로 하나 만듬
    private void saveUserFile() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("userlist.ser"))){
            objectOutputStream.writeObject(userList);
            System.out.println("직렬화 완료: userlist.ser");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
