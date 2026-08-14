package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> data = new HashMap<>();

    @Override
    public Message save(Message message) {
        data.put(message.getId(), message);
        log.debug("JCF 메시지 데이터 저장 : id={}", message.getId());

        return message;
    }

    @Override
    public Message findById(UUID id) {
        Message message = Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메시지입니다."));
        log.debug("JCF 메시지 데이터 조회 : id={}", id);

        return message;
    }

    @Override
    public List<Message> findAll() {
        List<Message> messages = data.values()
                .stream()
                .toList();
        log.debug("JCF 메시지 전체 조회 : count={}", data.size());

        return messages;
    }

    @Override
    public void delete(UUID id) {
        Message targetMessage = Optional.ofNullable(data.get(id))
                        .orElseThrow(() -> new IllegalArgumentException("삭제할 메시지가 없습니다."));

        data.remove(targetMessage.getId());
        log.debug("JCF 메시지 데이터 삭제 : id={}", id);
    }
}
