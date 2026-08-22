package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreate;
import com.sprint.mission.discodeit.dto.channelDto.ChannelResponse;
import com.sprint.mission.discodeit.dto.channelDto.ChannelRequest;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public ChannelResponse createPrivate(PublicChannelCreate publicChannelCreate) {
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
    public ChannelResponse update(ChannelRequest channelRequest) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public List<ChannelResponse> findAll() {
        return List.of();
    }

    @Override
    public List<ChannelResponse> findByUserId(UUID userId) {
        return List.of();
    }
}