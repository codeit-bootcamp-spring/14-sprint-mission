package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public record MessageCreateRequestDto(
    String content,
    UUID authorId,
    UUID channelId
) {
    public Message toEntity(Channel channel, User author, List<BinaryContent> attachments) {
        return new Message(this.content, channel, author, attachments);
    }


}
