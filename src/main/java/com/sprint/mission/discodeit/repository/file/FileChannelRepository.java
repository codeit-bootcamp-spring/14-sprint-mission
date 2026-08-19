package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
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
    public Channel updateName(UUID id, String name) {
        Channel updated = super.buffer.get(id).updateName(name);
        super.writeFromBufferToFile();
        return updated;
    }
}
