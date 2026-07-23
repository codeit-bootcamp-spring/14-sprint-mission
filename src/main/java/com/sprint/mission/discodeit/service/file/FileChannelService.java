package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {
    private final FileChannelRepository channelRepository;

    public FileChannelService() {
        this.channelRepository = new FileChannelRepository();
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
