package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {
    private static final String FILE_NAME = "messages.dat";

    private Map<UUID, Message> loadData() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new HashMap<>();
        }
    }

    private void saveData(Map<UUID, Message> data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류가 발생하였습니다.");
        }
    }

    @Override
    public Message save(Message message) {
        Map<UUID, Message> data = loadData();
        data.put(message.getId(), message);

        saveData(data);

        return message;
    }

    @Override
    public Message findById(UUID id) {
        return loadData().get(id);
    }

    @Override
    public List<Message> findAll() {
        return loadData().values()
                .stream()
                .toList();
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Message> data = loadData();
        data.remove(id);

        saveData(data);
    }
}
