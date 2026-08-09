package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {
    final List<Message> messageList;

    public FileMessageRepository() {
        this.messageList = new ArrayList<>();
    }

    @Override
    public void save(Message message) {
        messageList.add(message);
        saveMessage();
    }

    @Override
    public Message findById(UUID id) {
        for (Message each : messageList) {
            if (each.getId().equals(id)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public List<Message> findAll() {
        return messageList;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageList.stream()
                .filter(each -> each.getChannelId() != null && each.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void delete(UUID id) {
        Message target = findById(id);
        if (target != null) {
            messageList.remove(target);
            saveMessage();
        }
    }

    private void saveMessage() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("newmessagelist"))) {
            objectOutputStream.writeObject(messageList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
