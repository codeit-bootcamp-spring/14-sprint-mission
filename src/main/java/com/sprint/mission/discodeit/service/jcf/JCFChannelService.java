package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    // JCF(Map)를 활용하여 데이터를 저장할 수 있는 필드(data)를 final로 선언
    private final Map<UUID, Channel> data;

    // 생성자에서 초기화
    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    // data 필드를 활용해 생성, 조회, 수정, 삭제하는 메소드 구현
    // 채널 생성
    @Override
    public Channel createChannel(String name, String description) {
        Channel channel = new Channel(name, description);
        data.put(channel.getId(), channel);
        return channel;
    }

    // 채널 상세 조회
    @Override
    public Channel readChannel(UUID id) {
        Channel channel = data.get(id);
        if (channel == null) {
            throw new IllegalArgumentException("해당 채널은 존재하지 않습니다.");
        }
        return channel;
    }

    // 채널 전체 조회
    @Override
    public List<Channel> readAllChannels() {
        return new ArrayList<>(data.values());
    }

    // 채널 수정
    @Override
    public Channel updateChannel(UUID id, String name, String description) {
        Channel channel = data.get(id);
        if (channel != null) {
            channel.update(name, description);
        }
        return channel;
    }

    // 채널 삭제
    @Override
    public void deleteChannel(UUID id) {
        data.remove(id);
    }
}
