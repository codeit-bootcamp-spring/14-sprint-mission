package com.sprint.mission.discodeit.channel.application.provided.query;

import com.sprint.mission.discodeit.channel.application.dto.ChannelDto;
import java.util.List;
import java.util.UUID;

public interface ChannelFinder {

  List<ChannelDto> getAllByUserId(UUID userId);
}
