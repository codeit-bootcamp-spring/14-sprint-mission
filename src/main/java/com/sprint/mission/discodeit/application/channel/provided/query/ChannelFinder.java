package com.sprint.mission.discodeit.application.channel.provided.query;

import com.sprint.mission.discodeit.application.channel.dto.ChannelDto;
import java.util.List;
import java.util.UUID;

public interface ChannelFinder {

  List<ChannelDto> getAllByUserId(UUID userId);
}
