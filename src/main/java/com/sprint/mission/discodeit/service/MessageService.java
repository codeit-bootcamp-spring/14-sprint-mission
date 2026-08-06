package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface MessageService {

    // Create
    public Message createMessage(String values, Channel channel, User sender);

    // Read (id를 받아 Message 반환)
    public Message readMessage(UUID id);

    // Read (전체 조회)
    public List<Message> readAllMessage();

    // Update (메시지 내용 변경)
    public void updateMessage(UUID id, String values);

    // Delete (메시지 삭제)
    public void deleteMessage(UUID id);
}
