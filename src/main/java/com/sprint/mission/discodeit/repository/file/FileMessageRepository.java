package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {

    @Override
    public void save(Message message) {
        try {
            Files.createDirectories(Paths.get("./message"));
        }catch (IOException e){
            e.printStackTrace();
        }

        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("./message/" + message.getId() + ".ser"))) {

            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //생 객체를 넣어버려서
    @Override
    public Message findById(UUID id) {
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream("./message/" + id +".ser"))){
            return (Message) input.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        throw new NullPointerException("값이 없습니다.");
    }

    @Override
    public List<Message> findAll() {
        List<Message> lists = new ArrayList<>();
        // 해당 위치 파일 다 긁어 오기
        File[] files = new File("./message").listFiles();

        for (File file : files) {
            try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))){
                lists.add((Message)input.readObject());
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }


        return lists;
    }

    @Override
    public void deleteById(UUID id) {
        try{
            Files.deleteIfExists(Path.of("./message/" + id + ".ser"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
