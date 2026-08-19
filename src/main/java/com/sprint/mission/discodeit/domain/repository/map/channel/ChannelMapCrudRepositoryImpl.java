package com.sprint.mission.discodeit.domain.repository.map.channel;

import com.sprint.mission.discodeit.domain.entity.Channel;
import com.sprint.mission.discodeit.domain.entity.ChannelType;
import com.sprint.mission.discodeit.domain.repository.map.AbstractMapCrudRepository;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class ChannelMapCrudRepositoryImpl extends AbstractMapCrudRepository<Channel> implements ChannelRepository {

    @Override
    public List<Channel> findAllChannelByIds(List<UUID> idList) {
        List<Channel> channelList = super.findAllEntity();
        return channelList.stream()
            .filter(channel -> idList.contains(channel.getId()))
            .toList();
    }

    @Override
    public List<Channel> findAllPublicChannel() {
        List<Channel> channelList = super.findAllEntity();
        return channelList.stream()
            .filter(channel -> channel.getChannelType().equals(ChannelType.PUBLIC_CHANNEL))
            .toList();
    }
}
