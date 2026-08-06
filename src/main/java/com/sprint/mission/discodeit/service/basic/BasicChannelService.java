package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(ChannelType type, String channelName, String description) {
        Channel channel = new Channel(type, channelName, description);
        Channel savedChannel = channelRepository.save(channel);
        log.info("채널 생성 완료 : id={}", savedChannel.getId());

        return savedChannel;
    }

    @Override
    public Channel findById(UUID id) {
        Channel channel = channelRepository.findById(id);
        log.info("채널 조회 : id={}", channel.getId());

        return channel;
    }

    @Override
    public List<Channel> findAll() {
        List<Channel> channels = channelRepository.findAll();
        log.info("채널 전체 조회 : count={}", channels.size());

        return channels;
    }

    @Override
    public Channel update(UUID id, ChannelType type, String channelName, String description) {
        Channel targetChannel = channelRepository.findById(id);

        targetChannel.update(type, channelName, description);

        Channel updatedChannel = channelRepository.save(targetChannel);
        log.info("채널 수정 완료 : id={}", updatedChannel.getId());

        return updatedChannel;
    }

    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);
        log.info("채널 삭제 완료 : id={}", id);
    }
}
