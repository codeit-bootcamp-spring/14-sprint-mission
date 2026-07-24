package com.sprint.mission.discodeit.file.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.file.repository.ChatRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FilechatRepository implements ChatRepository {
    protected final static List<Message> messages = new ArrayList<>();

    @Override
    public void chatLoad() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("data/Message.ser"))){
            messages.addAll((List<Message>) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("기존 메시지 데이터가 없습니다.");
        }
    }

    @Override
    public void chatFlush() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("data/Message.ser"))) {
            objectOutputStream.writeObject((List<Message>)this.messages);
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("저장할 메시지가 없습니다.");
        }
    }

    @Override
    public void messageAdd(Message message) {
        this.messages.add(message);
    }

    @Override
    public Optional<Message> findByMessage(UUID messageID) {
        return messages.stream()
                .filter(messages -> messageID.equals(messages.getMessageId()))
                .findFirst();
    }

    @Override
    public void delete(Message message) {
        messages.remove(message);
    }

    @Override
    public List<Message> findAllMessage() {
        return new ArrayList<>(messages);
    }
}
