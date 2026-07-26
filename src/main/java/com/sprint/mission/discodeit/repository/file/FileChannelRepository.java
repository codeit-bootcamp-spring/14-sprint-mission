package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {

    @Override
    public void save(Channel channel) {
        // 중복코드 줄이기
        try {
            Files.createDirectories(Paths.get("./channel"));
        }catch (IOException e){
            e.printStackTrace();
        }

        try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("./channel/" + channel.getId()+".ser"))){
            output.writeObject(channel);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //생 객체를 넣어버려서
    @Override
    public Channel findById(UUID id) {
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream("./channel/" + id +".ser"))){
            return (Channel) input.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        throw new NullPointerException("값이 없습니다.");
    }

    @Override
    public List<Channel> findAll() {
        List<Channel> lists = new ArrayList<>();
        // 해당 위치 파일 다 긁어 오기
        File[] files = new File("./channel").listFiles();

        for (File file : files) {
            try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))){
                lists.add((Channel)input.readObject());
            } catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }


        return lists;
    }

    @Override
    public void deleteById(UUID id) {
        try{
            Files.deleteIfExists(Path.of("./channel/" + id + ".ser"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}


// 중복ㅋ