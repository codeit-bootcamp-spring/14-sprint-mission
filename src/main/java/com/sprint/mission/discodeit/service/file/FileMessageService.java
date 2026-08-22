package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileMessageService implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public MessageResponse create(MessageCreateRequest messageCreateRequest) {
        return null;
    }

    @Override
    public List<MessageResponse> findByUser(UUID user) {
        return List.of();
    }

    @Override
    public List<MessageResponse> findByChannel(UUID channelId) {
        return List.of();
    }

    @Override
    public List<MessageResponse> findAll() {
        return List.of();
    }

    @Override
    public MessageResponse update(MessageRequest messageRequest) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }
}
