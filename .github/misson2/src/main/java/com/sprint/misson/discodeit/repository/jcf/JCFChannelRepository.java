package com.sprint.misson.discodeit.repository.jcf;

import com.sprint.misson.discodeit.entity.Channel;
import com.sprint.misson.discodeit.entity.ChannelType;
import com.sprint.misson.discodeit.repository.ChannelRepository;
import java.util.HashMap;
import java.util.Map;

public class JCFChannelRepository implements ChannelRepository {

    private final Map<String, Channel> channels = new HashMap<>();

    @Override
    public Channel create(ChannelType channelType, String channelName) {
        if (channels.containsKey(channelName)) {
            throw new RuntimeException("해당 채널명은 이미 사용중입니다.");
        }
        Channel channel = new Channel(channelType, channelName);
        channels.put(channelName, channel);
        return channel;
    }

    @Override
    public Channel read(String channelName) {
        if (!channels.containsKey(channelName)) {
            throw new RuntimeException("존재하지 않는 채널입니다.");
        }
        return channels.get(channelName);
    }

    @Override
    public void update(Channel channel, String updateName) {
        String channelName = channel.getChannelName();
        if (!channels.containsKey(channelName)) {
            throw new RuntimeException("존재하지 않는 채널입니다.");
        } else if (channels.containsKey(updateName)) {
            throw new RuntimeException("이미 존재하는 채널입니다.");
        }
        channels.remove(channelName);
        channel.updateChannelName(updateName);
        channels.put(channel.getChannelName(), channel);
        System.out.println("채널 이름 변경 완료. 변경 된 채널 이름 = " + channel.getChannelName());
    }

    @Override
    public void delete(String channelName) {
        if (!channels.containsKey(channelName)) {
            throw new RuntimeException("존재하지 않는 채널입니다.");
        }
        channels.remove(channelName);
        System.out.println("채널 삭제 완료.");
    }

    @Override
    public void readAll() {
        channels.values().stream()
            .forEach(channel -> System.out.println(channel));
    }

}
