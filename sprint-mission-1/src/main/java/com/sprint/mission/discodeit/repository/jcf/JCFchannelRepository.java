package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.channelRepository;

import java.util.ArrayList;
import java.util.List;


public abstract class JCFchannelRepository implements channelRepository {
    protected final List<Channel> channels = new ArrayList<>();

    @Override
    public void channelCreate(String channelName) {
        boolean channelExists = true;
        for (Channel each:channels){
            if (each.getChannelName().equals(channelName)){
                System.out.println("이미 생성된 카테고리의 이름입니다: "+channelName);
                channelExists = false;
            }
        }
        if (channelExists){
            System.out.println("카테고리 생성이 완료되었습니다: "+channelName);
            channels.add(new Channel(channelName));
        }
    }

    @Override
    public Channel findByChannel(String channelName) {
        for (Channel each:channels){
            if (each.getChannelName().equals(channelName)){
                return each;
            }
        }
        System.out.println("찾는 카테고리의 이름이 없습니다: "+channelName);
        return null;
    }

    @Override
    public void channelUpdate(String channelName, String updateChannelName) {
        Channel channel = findByChannel(channelName);
        if (channel != null){
            System.out.println(channelName+" 카테고리의 이름을 "+updateChannelName+"로 수정했습니다.");
            channel.setChannelName(updateChannelName);
        }
    }

    @Override
    public void channelDelete(String channelName) {
        Channel channel = findByChannel(channelName);
        if (channel != null){
            System.out.println(channelName+" 카테고리를 삭제했습니다.");
            channels.remove(channel);
        }
    }
}
