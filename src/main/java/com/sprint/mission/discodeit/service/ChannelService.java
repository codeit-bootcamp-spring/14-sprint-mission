package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    ChannelDto createPublic(PublicChannelCreateRequest request);

    ChannelDto createPrivate(PrivateChannelCreateRequest request);

    ChannelDto find(UUID channelId);

    /** PUBLIC 채널 전체 + 그 유저가 참여 중인 PRIVATE 채널만. */
    List<ChannelDto> findAllByUserId(UUID userId);

    /** PRIVATE 채널은 이름·설명이 없어 수정 대상이 아니다. */
    ChannelDto update(UUID channelId, ChannelUpdateRequest request);

    void delete(UUID channelId);
}
