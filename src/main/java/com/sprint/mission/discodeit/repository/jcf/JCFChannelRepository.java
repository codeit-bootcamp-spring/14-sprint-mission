package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class JCFChannelRepository implements ChannelRepository {
    final List<Channel> channelList;

    public JCFChannelRepository() {
        this.channelList = new ArrayList<>();
    }

    @Override
    public void save(Channel channel) {
        if (findById(channel.getId()) != null) {
            return;
        }
        channelList.add(channel);
    }

    @Override
    public Channel findById(UUID id) {
        for (Channel each : channelList) {
            if (each.getId().equals(id)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public List<Channel> findAll() {
        return channelList;
    }

    @Override
    public void delete(UUID id) {
        Channel target = findById(id);
        if (target != null) {
            channelList.remove(target);
        }
    }
}
