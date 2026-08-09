package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    // < CRUD 기능 >
    // 메세지 생성
    Message createMessage(String content, UUID userId, UUID channelId);
    // 메세지 상세 조회
    Message readMessage(UUID id);
    // 메세지 전체 조회
    List<Message> readAllMessages();
    // 메세지 수정
    Message updateMessage(UUID id, String content);
    // 메세지 삭제
    void deleteMessage(UUID id);
}
