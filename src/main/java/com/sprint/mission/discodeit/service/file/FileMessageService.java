package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FileMessageService implements MessageService {
    final List<Message> messageList;
    private UserService userService;
    private ChannelService channelService;

    public FileMessageService(ChannelService channelService, UserService userService) {
        this.messageList = new ArrayList<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    // Create
    //심화 요구사항에 맞게 의존성 주입하고 검증
    public Message createMessage(String values, Channel channel, User sender) {
        //실존 유저인지, 채널있는지 검증
        if (Objects.isNull(userService.readUser(sender.getId())) ||
                Objects.isNull(channelService.readChannel(channel.getId()))) {
            throw new RuntimeException("유효하지 않은 채널 또는 유저입니다");
        }
        //샌더가 해당 채널에 존재하는지
        for (User each : channel.getMembers()) {
            if (each.equals(userService.readUser(sender.getId()))) {
                Message message = new Message(values, channel, sender);
                messageList.add(message);

                saveMessageFile(); //영속화

                return message;
            }
        }
        throw new RuntimeException("해당 채널의 멤버가 아닙니다" + userService.readUser(sender.getId()));
//            Message message = new Message(values, channel, sender);
//            messageList.add(message);
//            return message;

    }


    @Override
    // Read (id를 받아 Message 반환)
    public Message readMessage(UUID id) {
        for (Message each : messageList) {
            if (each.getId().equals(id)) {
                return each;
            }
        }
        return null;
    }

    @Override
    // Read (전체 조회)
    public List<Message> readAllMessage() {
        return this.messageList;
    }

    @Override
    // Update
    public void updateMessage(UUID id, String values) {
        Message message = readMessage(id);
        message.setValues(values);
        message.setUpdatedAt();

        saveMessageFile();
    }

    @Override
    // Delete
    public void deleteMessage(UUID id) {
        Message message = readMessage(id);
        messageList.remove(message);

        saveMessageFile();
    }

    public void saveMessageFile() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("newmessagelist.ser"))) {
            objectOutputStream.writeObject(messageList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
