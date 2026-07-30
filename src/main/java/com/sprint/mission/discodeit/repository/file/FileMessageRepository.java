package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {

    private final String MESSAGE_FILENAME = "messages.ser";
    private Map<UUID, Message> messageMap = loadFile(MESSAGE_FILENAME);

    private FileMessageRepository() {}

    private static class LazyHolder {
        private final static FileMessageRepository INSTANCE = new FileMessageRepository();
    }

    public static FileMessageRepository getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public Message save(Message message) {
        messageMap.put(message.getId(), message);
        saveToFile(messageMap, MESSAGE_FILENAME);
        return message;
    }

    @Override
    public Message find(UUID id) {
        return Optional.ofNullable(messageMap.get(id))
                .orElseThrow(() -> new CustomException(ExceptionType.MESSAGE_NOT_FOUND));
    }

    @Override
    public List<Message> findAll() {
        return messageMap.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        if (messageMap.remove(id) == null) {
            throw new CustomException(ExceptionType.MESSAGE_NOT_FOUND);
        }
        saveToFile(messageMap, MESSAGE_FILENAME);
    }



    /**
     * Helper
     */

    private Map<UUID, Message> loadFile(String filename) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filename))) {
            return (Map<UUID, Message>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveToFile(Map<UUID, Message> messageMap, String filename) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            objectOutputStream.writeObject(messageMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
