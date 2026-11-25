package com.sprint.mission.discodeit.channel.adapter.api.request;


public record ChannelUpdateRequest(
    String newName,
    String newDescription
) {

}
