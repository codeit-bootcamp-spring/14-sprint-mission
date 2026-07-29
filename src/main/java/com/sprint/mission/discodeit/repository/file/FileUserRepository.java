package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserRepository implements UserRepository {

    @Override
    public void save(User user) {
        try {
            Files.createDirectories(Paths.get("./user"));
        }catch (IOException e){
            e.printStackTrace();
        }

        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("./user/"+user.getId()+".ser"))) {
            output.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //생 객체를 넣어버려서
    @Override
    public User findById(UUID id) {
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream("./user/" + id +".ser"))){
            return (User) input.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        throw new NullPointerException("값이 없습니다.");
    }

    @Override
    public List<User> findAll() {
        List<User> lists = new ArrayList<>();
        // 해당 위치 파일 다 긁어 오기
        File[] files = new File("./user").listFiles();

        for (File file : files) {
            try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))){
                lists.add((User)input.readObject());
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }


        return lists;
    }

    @Override
    public void deleteById(UUID id) {
        try{
            Files.deleteIfExists(Path.of("./user/" + id + ".ser"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
