package com.sprint.mission.discodeit.message.security;

import com.sprint.mission.discodeit.message.repository.MessageRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageAuthorization {

    private final MessageRepository messageRepository;

    // 메시지가 없으면 true로 통과시켜 서비스에서 MESSAGE_NOT_FOUND(404)를 내게 한다.
    // 탈퇴로 작성자가 비워진(null) 메시지는 아무도 수정·삭제할 수 없다
    public boolean isAuthor(UUID messageId, UUID userId) {
        return messageRepository.findById(messageId)
            .map(message -> message.getAuthor() != null
                && message.getAuthor().getId().equals(userId))
            .orElse(true);
    }
}
