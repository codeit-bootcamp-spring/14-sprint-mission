package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new LinkedHashMap<>();
    }

    @Override
    public Channel create(String channelName, String description) {
        Channel createChannel = new Channel(channelName, description);
        data.put(createChannel.getId(), createChannel);
        return data.get(createChannel.getId());
    }

    @Override
    public Channel findById(UUID id) {
        Channel findChannel = data.get(id);
        if (findChannel == null) {
            throw new IllegalArgumentException("채널을 찾을 수 없습니다! id: " + id);
        }

        return data.get(id);

    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Channel update(UUID id, String username, String email) {
        Channel updateChannel = findById(id);
        updateChannel.update(username, email);
        return updateChannel;
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
