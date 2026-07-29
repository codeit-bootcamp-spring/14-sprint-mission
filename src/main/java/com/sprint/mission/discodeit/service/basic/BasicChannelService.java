package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository){
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(ChannelType ChannelType, String memo, String memoText) {
        Channel channel = new Channel(ChannelType, memo, memoText);
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel read(UUID id) {

        return check(id);
    }

    @Override
    public void update(Channel t) {
        Channel channel1 = check(t.getId());
        channel1.update(t.getChannelType(), t.getMemo(), t.getMemoText());
        channelRepository.save(channel1);
    }

    @Override
    public void delete(UUID id) {
        check(id);
        channelRepository.deleteById(id);
    }

    @Override
    public List<Channel> readAll() {
        return  channelRepository.findAll();
    }


    private Channel check(UUID id) {
        Channel channel = channelRepository.findById(id);
        if (channel == null) {
            throw new IllegalArgumentException("해당 값이 존재하지 않습니다.");
        }
        return channel;
    }
}
