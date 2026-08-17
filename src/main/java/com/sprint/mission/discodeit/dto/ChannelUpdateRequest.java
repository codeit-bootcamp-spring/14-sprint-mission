package com.sprint.mission.discodeit.dto;

/** PUBLIC 채널만 수정 가능하다. PRIVATE은 이름·설명이 없어 수정할 게 없다. */
public record ChannelUpdateRequest(
        String newChannelName,
        String newDescription
) {
}
