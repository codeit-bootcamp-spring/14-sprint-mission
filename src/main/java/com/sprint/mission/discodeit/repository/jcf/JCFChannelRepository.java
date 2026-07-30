package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channelMap = new HashMap<>();

    private JCFChannelRepository() {}

    private static class LazyHolder {
        private static final JCFChannelRepository INSTANCE = new JCFChannelRepository();
    }

    public static JCFChannelRepository getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public Channel save(Channel user) {
        this.channelMap.put(user.getId(), user);
        return user;
    }

    @Override
    public Channel find(UUID id) {
        return Optional.ofNullable(this.channelMap.get(id))
                .orElseThrow(() -> new CustomException(ExceptionType.CHANNEL_NOT_FOUND));
    }

    @Override
    public List<Channel> findAll() {
        return this.channelMap.values().stream().toList();
    }

    @Override
    public void delete(UUID id) {
        if (this.channelMap.remove(id) == null) {
            throw new CustomException(ExceptionType.CHANNEL_NOT_FOUND);
        }
    }
}
