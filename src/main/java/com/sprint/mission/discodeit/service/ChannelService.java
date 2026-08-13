package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.dto.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelResponse createPublic(ChannelCreatePublicRequest request);
    ChannelResponse createPrivate(ChannelCreatePrivateRequest request);
    ChannelResponse find(UUID channelId);

    List<ChannelResponse> findAllByUserId(UUID userId);

    ChannelResponse update(ChannelUpdateRequest request);
    void delete(UUID channelId);
}
