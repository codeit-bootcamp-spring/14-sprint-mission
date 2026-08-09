package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileChannelRepository implements ChannelRepository {

    @Override
    public void save(Channel channel) {
        // 중복코드 줄이기
        try {
            Files.createDirectories(Paths.get("./channel"));
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        try (FileOutputStream fos = new FileOutputStream("./channel/" + channel.getId()+".ser");
             ObjectOutputStream output = new ObjectOutputStream(fos)) {
            output.writeObject(channel);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    //생 객체를 넣어버려서
    @Override
    public Optional<Channel> findById(UUID id) {
        try (FileInputStream fis = new FileInputStream("./channel/" + id +".ser");
             ObjectInputStream input = new ObjectInputStream(fis)) {
            return Optional.ofNullable((Channel) input.readObject());
        } catch (IOException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Channel> findAll() {
        List<Channel> lists = new ArrayList<>();
        // 해당 위치 파일 다 긁어 오기
        File[] files = new File("./channel").listFiles((dir, name) -> name.endsWith(".ser"));

        for (File file : files) {
            try (FileInputStream fis = new FileInputStream(file);
                 ObjectInputStream input = new ObjectInputStream(fis)) {
                lists.add((Channel)input.readObject());
            } catch (IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }


        return lists;
    }

    @Override
    public void deleteById(UUID id) {
        try{
            Files.deleteIfExists(Path.of("./channel/" + id + ".ser"));
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}


// 중복ㅋ