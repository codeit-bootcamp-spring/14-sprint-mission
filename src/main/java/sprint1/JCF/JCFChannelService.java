package sprint1.JCF;

import com.example.demo.levelTest1.entity.Channel;
import com.example.demo.levelTest1.entity.ChannelType;
import com.example.demo.levelTest1.service.ChannelService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        this.data.put(channel.getId(), channel);

        return channel;
    }

    @Override
    public Channel find(UUID channelId) {
        Channel channelNullable = this.data.get(channelId);

        return Optional.ofNullable(channelNullable)
            .orElseThrow(
                () -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<Channel> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public Channel update(UUID channelId, ChannelType newChannel) {
        Channel channelNullable = this.data.get(channelId);
        Channel channel = Optional.ofNullable(channelNullable)
            .orElseThrow(
                () -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        channel.update(newChannel);

        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        if (!this.data.containsKey(channelId)) {
            throw new NoSuchElementException(
                new NoSuchElementException("Channel with id " + channelId + " not found"));

        }
        this.data.remove(channelId);
    }
}
