package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf"
)
public class JCFChannelRepository extends AbstractJCFRepository<Channel>
        implements ChannelRepository {

    @Override
    public Channel updateNameAndDescription(UUID id, String name, String description) {
        return super.STORE.get(id).updateNameAndDescription(name, description);
    }

    @Override
    public boolean existsById(UUID id) {
        return super.STORE.values().stream()
                .anyMatch(channel -> channel.getId().equals(id));
    }
}
