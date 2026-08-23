package com.sprint.mission.discodeit.channel.repository.file;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

    private final Path directory;

    public FileChannelRepository(
            @Value("${discodeit.repository.file-directory}") String fileDirectory) {
        this.directory = Paths.get(fileDirectory, "channel");
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Channel channel) {
        try (FileOutputStream fos = new FileOutputStream(directory.resolve(channel.getId() + ".ser").toFile());
             ObjectOutputStream output = new ObjectOutputStream(fos)) {
            output.writeObject(channel);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    //생 객체를 넣어버려서
    @Override
    public Optional<Channel> findById(UUID id) {
        try (FileInputStream fis = new FileInputStream(directory.resolve(id + ".ser").toFile());
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
        File[] files = directory.toFile().listFiles((dir, name) -> name.endsWith(".ser"));

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
            Files.deleteIfExists(directory.resolve(id + ".ser"));
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}


// 중복ㅋ