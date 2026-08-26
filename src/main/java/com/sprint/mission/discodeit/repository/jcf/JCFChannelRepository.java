package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFChannelRepository extends JCFRepository<Channel> implements ChannelRepository {

    private JCFChannelRepository() {
        super();
    }

    @Override
    public List<Channel> findAllByType(ChannelType type) {
        return findAll().stream().
            filter(channel -> channel.getType() == type)
            .toList();

    }


}
