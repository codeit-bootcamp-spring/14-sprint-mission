package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    //Create(채널 생성)
    public Channel createChannel(String channelName, User ... members);

    //Read (채널 조회)
    public Channel readChannel(UUID id);

    //Read (전체 채널 목록 조회)
    public List<Channel> readAllChannel();

    //Update (채널 id받고 채널 이름, 채널 멤버 변경)
    public void updateChannel(UUID id, String channelName, User ... members);

    //Delete (채널 삭제)
    public void deleteChannel(UUID uuid);

}
