package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Server;

import java.util.List;

public interface serverService {
    // 서버 목록 출력
    void allPrintServer();
    void printServer();
    // 서버 선택
    Server selectedServer();
}
