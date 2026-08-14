package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        log.debug("JCF 채널 데이터 저장 : id={}", channel.getId());

        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        Channel channel = Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));
        log.debug("JCF 채널 데이터 조회 : id={}", id);

        return channel;
    }

    @Override
    public List<Channel> findAll() {
        List<Channel> channels = data.values()
                .stream()
                .toList();
        log.debug("JCF 채널 전체 조회 : count={}", data.size());

        return channels;
    }

    @Override
    public void delete(UUID id) {
        Channel targetChannel = Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new IllegalArgumentException("삭제할 채널이 없습니다."));

        data.remove(targetChannel.getId());
        log.debug("JCF 채널 데이터 삭제 : id={}", id);
    }
}
