package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public ChannelResponseDto create(ChannelCreateRequestDto requestDto) {
        Channel newChannel = Channel.from(requestDto);
        channelRepository.save(newChannel);
        return ChannelResponseDto.from(newChannel);
    }

    @Override
    public ChannelResponseDto read(UUID id) {
        return ChannelResponseDto.from(
                channelRepository.find(id)
        );
    }

    @Override
    public List<ChannelResponseDto> readAll() {
        return channelRepository.findAll().stream()
                .map(ChannelResponseDto::from)
                .toList();
    }

    @Override
    public ChannelResponseDto update(ChannelUpdateRequestDto requestDto) {
        Channel channelToUpdate = channelRepository.find(requestDto.getId());
        channelToUpdate.update(requestDto.getName(), requestDto.getChannelType());
        channelRepository.save(channelToUpdate);
        return ChannelResponseDto.from(channelToUpdate);
    }

    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);
    }
}
