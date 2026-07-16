package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

public interface channelService {
    // 서버 목록 출력
    void allPrintChannel();
    void printChannel();
    // 서버 선택
    Channel selectedChannel();
}
