package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Server;

public interface serverRepository extends serverService{
    void serverCreate(String serverName);
    Server findByServer(String serverName);
    void serverUpdate(String serverName, String updateServerName);
    void serverDelete(String serverName);
}
