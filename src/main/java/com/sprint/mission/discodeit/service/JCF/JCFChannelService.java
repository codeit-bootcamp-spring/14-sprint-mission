package com.sprint.mission.discodeit.service.JCF;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class JCFChannelService implements ChannelService {

    private final List<Channel> channels = new ArrayList<>();

    @Override
    public Channel create(Channel channel) {
        channels.add(channel);
        return channel;
    }

    @Override
    public Channel find(UUID id) {
        for (Channel channel : channels){
            if(channel.getId().equals(id)){
                return channel;
            }
        }
        return null;
    }

    @Override
    public List<Channel> findAll() {
        return channels;
    }

    @Override
    public Channel update(Channel channel) {
        Channel found = find(channel.getId());

        if (found != null){
            found.update(channel.getName(), channel.getChannelNum());
        }
        return found;
    }

    @Override
    public void delete(UUID id) {

        Channel found  = find(id);

        if (found !=null){
            channels.remove(found);
        }
    }
}