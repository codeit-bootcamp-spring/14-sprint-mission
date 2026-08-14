package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    // 저장(생성 및 수정)
    Message save(Message message);

    // 단건 조회
    Message findById(UUID id);

    // 전체 조회
    List<Message> findAll();

    // 삭제
    void delete(UUID id);
}
