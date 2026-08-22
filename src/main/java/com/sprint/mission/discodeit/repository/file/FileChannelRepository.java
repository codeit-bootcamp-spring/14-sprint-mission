package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file"
)
public class FileChannelRepository extends AbstractFileRepository<Channel>
        implements ChannelRepository {

    public FileChannelRepository() {
        super(Files.CHANNEL);
    }

    @Override
    public Channel updateNameAndDescription(UUID id, String name, String description) {
        Channel updated = super.buffer.get(id).updateNameAndDescription(name, description);
        super.writeFromBufferToFile();
        return updated;
    }

    @Override
    public boolean existsById(UUID id) {
        return super.buffer.values().stream()
                .anyMatch(channel -> channel.getId().equals(id));
    }
}
