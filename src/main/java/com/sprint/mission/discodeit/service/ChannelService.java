package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreate;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreate;
import com.sprint.mission.discodeit.dto.channelDto.ChannelResponse;
import com.sprint.mission.discodeit.dto.channelDto.ChannelRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface ChannelService {


    ChannelResponse createPrivate(PrivateChannelCreate privateChannelCreate);

    ChannelResponse createPublic(PublicChannelCreate publicChannelCreate);

    ChannelResponse findChannel(UUID uuid);

    ChannelResponse update(UUID id, ChannelRequest channelRequest);

    void delete(UUID uuid);


    List<ChannelResponse> findByUserId(UUID userId);
}
