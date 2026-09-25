package com.sprint.mission.discodeit.application.channel;

import com.sprint.mission.discodeit.adaptor.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.adaptor.dto.channelDto.ChannelRequest;
import com.sprint.mission.discodeit.adaptor.dto.channelDto.ChannelResponse;
import com.sprint.mission.discodeit.adaptor.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.adaptor.dto.channelDto.PublicChannelCreateRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface ChannelService {


  ChannelResponse createPrivate(PrivateChannelCreateRequest privateChannelCreateRequest);

  ChannelResponse createPublic(PublicChannelCreateRequest publicChannelCreateRequest);

  ChannelDto findChannel(UUID uuid);

  ChannelResponse update(UUID id, ChannelRequest channelRequest);

  void delete(UUID uuid);


  List<ChannelDto> findByUserId(UUID userId);
}
