package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.serverService;

public interface serverRepository extends serverService {
    void serverCreate(String serverName);
    Server findByServer(String serverName);
    void serverUpdate(String serverName, String updateServerName);
    void serverDelete(String serverName);
}
