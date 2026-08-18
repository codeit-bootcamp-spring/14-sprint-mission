package sprint1.JCF;

import sprint1.entity.Message;
import sprint1.service.ChannelService;
import sprint1.service.MessageService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import sprint1.service.UserService;

public class JCFMessageService implements MessageService {

    private final Map<UUID, Message> data;

    public JCFMessageService() {
        this.data = new HashMap<>();
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {

        ChannelService channelService = new JCFChannelService();
        UserService userService = new JCFUserService();
        try {
            channelService.find(channelId);
            userService.find(authorId);
        } catch (NoSuchElementException e) {
            throw e;
        }


        Message message = new Message(content, channelId, authorId);
        this.data.put(message.getId(), message);

        return message;
    }

    @Override
    public Message find(UUID messageId) {
        Message messageNullable = this.data.get(messageId);

        return Optional.ofNullable(messageNullable)
            .orElseThrow(
                () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<Message> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message messageNullable = this.data.get(messageId);
        Message message = Optional.ofNullable(messageNullable)
            .orElseThrow(
                () -> new NoSuchElementException("Message with id " + messageId + " not found"));

        message.update(newContent);

        return message;
    }

    @Override
    public void delete(UUID messageId) {
        if (!this.data.containsKey(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        this.data.remove(messageId);
    }
}
