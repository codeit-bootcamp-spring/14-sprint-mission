package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public FileChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(Channel channel) {
        channelRepository.save(channel);
        System.out.println("채널 등록이 완료되었습니다.");

        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        Channel channel = Optional.ofNullable(channelRepository.findById(id))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));

        return channel;
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID id, Channel channel) {
        Channel updatedChannel = Optional.ofNullable(channelRepository.findById(id))
                .orElseThrow(() -> new IllegalArgumentException("수정할 채널이 없습니다."));

        updatedChannel.setChannelName(channel.getChannelName());
        updatedChannel.setDescription(channel.getDescription());

        channelRepository.save(updatedChannel);
        System.out.println("채널 정보 수정이 완료되었습니다.");

        return updatedChannel;
    }

    @Override
    public void delete(UUID id) {
        Channel deletedChannel = Optional.ofNullable(channelRepository.findById(id))
                .orElseThrow(() -> new IllegalArgumentException("삭제할 채널이 없습니다."));

        channelRepository.delete(id);
        System.out.println("채널 정보 삭제가 완료되었습니다.");
    }
}
