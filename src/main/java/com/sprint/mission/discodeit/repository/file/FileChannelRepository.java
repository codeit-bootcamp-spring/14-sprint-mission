package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileChannelRepository
        extends AbstractFileRepository<Channel>
        implements ChannelRepository {

    private static final String CHANNEL_FILENAME = "channels.ser";

    private final Map<UUID, Channel> channelMap;

    public FileChannelRepository(
            @Value("${discodeit.repository.file-directory:.discodeit/objects}")
            String fileDirectory
    ) {
        super("Channel", fileDirectory, CHANNEL_FILENAME);
        this.channelMap = loadFile();
    }

    @Override
    public Channel save(Channel channel) {
        Channel previousChannel = channelMap.put(channel.getId(), channel);
        try {
            saveFile(channelMap);
        } catch (RuntimeException exception) {
            if (previousChannel == null) {
                channelMap.remove(channel.getId());
            } else {
                channelMap.put(previousChannel.getId(), previousChannel);
            }
            throw exception;
        }
        return channel;
    }

    @Override
    public Optional<Channel> findById(UUID channelId) {
        return Optional.ofNullable(channelMap.get(channelId));
    }

    @Override
    public List<Channel> findAll() {
        return channelMap.values().stream().toList();
    }

    @Override
    public void delete(UUID channelId) {
        Channel deletedChannel = channelMap.remove(channelId);
        try {
            saveFile(channelMap);
        } catch (RuntimeException exception) {
            if (deletedChannel != null) {
                channelMap.put(deletedChannel.getId(), deletedChannel);
            }
            throw exception;
        }
    }

}
