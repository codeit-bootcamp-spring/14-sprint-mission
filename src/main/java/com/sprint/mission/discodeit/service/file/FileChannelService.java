package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreate;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreate;
import com.sprint.mission.discodeit.dto.channelDto.ChannelResponse;
import com.sprint.mission.discodeit.dto.channelDto.ChannelRequest;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
//@Service
public class FileChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public ChannelResponse createPrivate(PrivateChannelCreate privateChannelCreate) {
        return null;
    }

    @Override
    public ChannelResponse createPublic(PublicChannelCreate publicChannelCreate) {
        return null;
    }

    @Override
    public ChannelResponse findChannel(UUID uuid) {
        return null;
    }

    @Override
    public ChannelResponse update(UUID id, ChannelRequest channelRequest) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }


    @Override
    public List<ChannelResponse> findByUserId(UUID userId) {
        return List.of();
    }
}