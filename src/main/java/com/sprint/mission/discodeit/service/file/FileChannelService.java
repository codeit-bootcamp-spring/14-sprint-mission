package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {

    FileChannelRepository fileChannelRepository;

    public FileChannelService(FileChannelRepository fileChannelRepository){
        this.fileChannelRepository = fileChannelRepository;
    }

    @Override
    public Channel create(ChannelType ChannelType, String memo, String memoText) {
        Channel channel = new Channel(ChannelType, memo, memoText);
        fileChannelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel read(UUID id) {
        return fileChannelRepository.findById(id);
    }

    @Override
    public void update(Channel t) {
        fileChannelRepository.save(t);
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public List<Channel> readAll() {
        return List.of();
    }
}
