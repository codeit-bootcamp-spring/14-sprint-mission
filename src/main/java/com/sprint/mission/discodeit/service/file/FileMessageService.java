package com.sprint.mission.discodeit.service.file;

import static com.sprint.mission.discodeit.service.basic.BasicMessageService.ERROR_MESSAGE_NOT_FOUND;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
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

public class FileMessageService implements MessageService {

    private static final String FILE_PATH = "messages.ser";

    private Map<UUID, Message> loadData(){
        File file = new File(FILE_PATH);
        if (!file.exists()){
            return new HashMap<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))){
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveData(Map<UUID, Message> data){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message create(UUID senderId, UUID channelId, String content) {
        Map<UUID, Message> data = loadData();
        Message message = Message.create(senderId, channelId, content);
        data.put(message.getId(), message);
        saveData(data);
        return message;
    }

    @Override
    public Optional<Message> read(UUID id) {
        Map<UUID, Message> data = loadData();
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Message> readAll() {
        Map<UUID, Message> data = loadData();
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, String content) {
        Map<UUID, Message> data = loadData();
        Message message = Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new IllegalArgumentException(ERROR_MESSAGE_NOT_FOUND + id));
            message.changeContent(content);
            saveData(data);
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Message> data = loadData();
        data.remove(id);
        saveData(data);
    }
}
