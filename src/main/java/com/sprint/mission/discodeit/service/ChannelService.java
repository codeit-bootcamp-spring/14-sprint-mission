package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    // 생성
    Channel create(Channel channel);

    // 생성(Overload)
//    Channel create(ChannelType type, String channelName, String description);

    // 단건 조회
    Channel findById(UUID id);

    // 전체 조회
    List<Channel> findAll();

    // 수정
    Channel update(UUID id, Channel channel);

    // 수정(Overload)
//    Channel update(UUID id, ChannelType type, String channelName, String description);

    // 삭제
    void delete(UUID id);
}
