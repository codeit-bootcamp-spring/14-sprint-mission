package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final JCFChannelRepository channelRepository;

    public JCFChannelService() {
        this.channelRepository = new JCFChannelRepository();
    }


    @Override
    public ChannelResponseDto create(ChannelCreateRequestDto requestDto) {
        Channel channel = Channel.from(requestDto);
        channelRepository.save(channel);
        return ChannelResponseDto.from(channel);
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
        channelRepository.save(channelToUpdate);    // 굳이인가요?
        return ChannelResponseDto.from(channelToUpdate);
    }

    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);
    }
}
