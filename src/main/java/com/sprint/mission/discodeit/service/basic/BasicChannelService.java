package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BasicChannelService  implements ChannelService {
    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository){
        this.channelRepository = channelRepository;
    }

    @Override
    public void create(Channel entity) {
        if(channelRepository.findById(entity.getId()) != null){
            throw new RuntimeException("이미 존재하는 채널입니다.");
        }
        channelRepository.save(entity);
    }

    @Override
    public Channel read(UUID id) {
        Channel channel = channelRepository.findById(id);
        if(Objects.isNull(channel)){
            throw new RuntimeException("존재하지 않는 채널입니다");
        }
        return channel;
    }

    @Override
    public void update(Channel channel, String newname) {
        read(channel.getId());
        channel.setName(newname);
        channelRepository.save(channel);
    }

    @Override
    public void delete(UUID id) {
        read(id);
        channelRepository.deleteById(id);
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }
}
