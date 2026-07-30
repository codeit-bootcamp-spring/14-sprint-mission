package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    // 저장(생성 및 수정)
    Channel save(Channel channel);

    // 단건 조회
    Channel findById(UUID id);

    // 전체 조회
    List<Channel> findAll();

    // 삭제
    void delete(UUID id);
}
