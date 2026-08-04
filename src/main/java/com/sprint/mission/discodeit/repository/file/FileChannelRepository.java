package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileChannelRepository extends AbstractFileRepository<Channel>
        implements ChannelRepository {

    public FileChannelRepository() {
        super(Files.CHANNEL);
    }

    @Override
    public void updateName(UUID id, String name) {
        findById(id).ifPresent(retrieved -> {
            retrieved.updateName(name);
            super.writeFromBufferToFile();
        });
    }

    @Override
    public void deleteUsersByUserId(UUID userId) {
        buffer.values()
                .forEach(channel -> channel.getUsersId().remove(userId));
        super.writeFromBufferToFile();
    }
}
