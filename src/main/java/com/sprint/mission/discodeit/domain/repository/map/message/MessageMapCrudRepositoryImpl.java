package com.sprint.mission.discodeit.domain.repository.map.message;

import com.sprint.mission.discodeit.domain.entity.Message;
import com.sprint.mission.discodeit.domain.repository.MessageRepository;
import com.sprint.mission.discodeit.domain.repository.map.AbstractMapCrudRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class MessageMapCrudRepositoryImpl extends AbstractMapCrudRepository<Message> implements
    MessageRepository {

    @Override
    public Optional<Message> findLastMessageByChannelId(UUID channelId) {
        List<Message> messageList = super.findAllEntity();
        return messageList.stream()
            .filter(message -> message.getChannelId().equals(channelId))
            .max(Comparator.comparing(Message::getCreatedAt));
    }

    @Override
    public void deleteMessageByChannelId(UUID channelId) {
        List<Message> messageList = super.findAllEntity();
        List<Message> filteredMessages = messageList.stream()
            .filter(message -> message.getChannelId().equals(channelId))
            .toList();

        /*
            CME 조심
         */
        for (Message filteredMessage : filteredMessages) {
            super.deleteEntity(filteredMessage.getId());
        }
    }

    @Override
    public List<Message> findAllMessageByChannelId(UUID channelId) {
        List<Message> messageList = super.findAllEntity();
        return messageList.stream()
            .filter(message -> message.getChannelId().equals(channelId))
            .toList();
    }
}
