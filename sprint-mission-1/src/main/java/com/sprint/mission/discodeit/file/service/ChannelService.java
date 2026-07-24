package com.sprint.mission.discodeit.file.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelService {
    void channelInit();
    void channelCreate();
    void channelUpdate(String channelName, String updateChannelName);
    void channelDelete(String channelName);
    List<Channel> allPrintChannel();
    void printChannel(String channelName);
}
