package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    // 생성
    Message create(Message message);

    // 단건 조회
    Message findById(UUID id);

    // 전체 조회
    List<Message> findAll();

    // 수정
    Message update(UUID id, Message message);

    // 삭제
    void delete(UUID id);
}
