package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;

public interface ChannelService {
    Channel channelCreate(String channelName);
    void channelUpdate(String channelName, String updateChannelName);
    void channelDelete(String channelName);
    List<Channel> allPrintChannel();
    void printChannel(String channelName);
}
