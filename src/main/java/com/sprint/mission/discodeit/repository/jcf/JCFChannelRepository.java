package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFChannelRepository implements ChannelRepository {
    final List<Channel> channelList;

    public JCFChannelRepository() {
        this.channelList = new ArrayList<>();
    }


    @Override
    //
    public void save(Channel channel) {
        channelList.add(channel);
    }

    // 채널 하나하나 순회하며 아이디 조회해보며 채널 가져오기
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
        channelList.remove(target);
    }
}
