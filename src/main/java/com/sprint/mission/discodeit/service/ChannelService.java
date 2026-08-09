package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    // < CRUD 기능 >
    // 채널 생성
    Channel createChannel(String name, String description);
    // 채널 상세 조회
    Channel readChannel(UUID id);
    // 채널 전체 조회
    List<Channel> readAllChannels();
    // 채널 수정
    Channel updateChannel(UUID id, String name, String description);
    // 채널 삭제
    void deleteChannel(UUID id);
}
