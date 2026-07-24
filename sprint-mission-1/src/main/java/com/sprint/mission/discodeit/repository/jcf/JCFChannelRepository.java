package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class JCFChannelRepository implements ChannelRepository {
    private final List<Channel> channels = new ArrayList<>();

    @Override
    public Channel channelAdd(Channel channel) {
        channels.add(channel);
        return channel;
    }

    @Override
    public Optional<Channel> findByChannel(String channelName) {
        return channels.stream()
                .filter(channel -> channelName.equals(channel.getChannelName()))
                .findFirst();
    }

    @Override
    public void delete(Channel channel) {
        channels.remove(channel);
    }

    @Override
    public List<Channel> findAllChannel() {
        return new ArrayList<>(channels);
    }
}
