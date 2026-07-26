package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {
    // JCF(Map)를 활용하여 데이터를 저장할 수 있는 필드(data)를 final로 선언
    private final Map<UUID, Message> data;
    private final UserService userService;
    private final ChannelService channelService;

    // 생성자에서 초기화 - 메세지는 유저와 채널이 있어야 생성 가능함
    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    // data 필드를 활용해 생성, 조회, 수정, 삭제하는 메소드 구현
    // 메세지 생성
    @Override
    public Message createMessage(String content, UUID userId, UUID channelId) {
        if (this.userService != null) {
            this.userService.readUser(userId);
        }
        if (this.channelService != null) {
            this.channelService.readChannel(channelId);
        }

        Message message = new Message(content, userId, channelId);
        data.put(message.getId(), message);
        return message;
    }

    // 메세지 상세 조회
    @Override
    public Message readMessage(UUID id) {
        Message message = data.get(id);
        if (message == null) {
            throw new IllegalArgumentException("해당 메세지가 존재하지 않습니다.");
        }
        return message;
    }

    // 메세지 전체 조회
    @Override
    public List<Message> readAllMessages() {
        return new ArrayList<>(data.values());
    }

    // 메세지 수정
    @Override
    public Message updateMessage(UUID id, String content) {
        Message message = data.get(id);
        if (message != null) {
            message.update(content);
        }
        return message;
    }

    // 메세지 삭제
    @Override
    public void deleteMessage(UUID id) {
        data.remove(id);
    }
}
