package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileMessageRepository
        extends AbstractFileRepository<Message>
        implements MessageRepository {

    private static final String MESSAGE_FILENAME = "messages.ser";

    private final Map<UUID, Message> messageMap;

    public FileMessageRepository(
            @Value("${discodeit.repository.file-directory:.discodeit/objects}")
            String fileDirectory
    ) {
        super("Message", fileDirectory, MESSAGE_FILENAME);
        this.messageMap = loadFile();
    }

    @Override
    public Message save(Message message) {
        Message previousMessage = messageMap.put(message.getId(), message);
        try {
            saveFile(messageMap);
        } catch (RuntimeException exception) {
            if (Objects.isNull(previousMessage)) {
                messageMap.remove(message.getId());
            } else {
                messageMap.put(previousMessage.getId(), previousMessage);
            }
            throw exception;
        }
        return message;
    }

    @Override
    public Optional<Message> findById(UUID messageId) {
        return Optional.ofNullable(messageMap.get(messageId));
    }

    @Override
    public Optional<Message> findMostRecentByChannelId(UUID channelId) {
        Message mostRecentMessage = null;

        for (Message currentMessage : messageMap.values()) {
            if (currentMessage.getChannelId().equals(channelId)) continue;

            // 가장 최근 보낸 메세지 선정 로직
            if (Objects.isNull(mostRecentMessage)
                    || currentMessage.getCreatedAt()
                        .isAfter(mostRecentMessage.getCreatedAt())) {
                mostRecentMessage = currentMessage;
            }
        }

        return Optional.ofNullable(mostRecentMessage);
    }

    @Override
    public List<Message> findAll() {
        return messageMap.values().stream().toList();
    }

    @Override
    public void delete(UUID messageId) {
        Message deletedMessage = messageMap.remove(messageId);
        try {
            saveFile(messageMap);
        } catch (RuntimeException exception) {
            if (Objects.nonNull(deletedMessage)) {
                messageMap.put(deletedMessage.getId(), deletedMessage);
            }
            throw exception;
        }
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        List<Message> messagesToDelete = new ArrayList<>();

        // channelId로 보내진 모든 메세지들을 모은다
        for (Message message : messageMap.values()) {
            if (Objects.equals(message.getChannelId(), channelId)) {
                messagesToDelete.add(message);
            }
        }

        // 그 메세지들을 하나하나 다 지운다
        for (Message message : messagesToDelete) {
            messageMap.remove(message.getId());
        }

        // 파일 저장
        try {
            saveFile(messageMap);
        } catch (RuntimeException exception) {
            for (Message message : messagesToDelete) {
                messageMap.put(message.getId(), message);
            }
            throw exception;
        }
    }

}
