package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final UserMapper userMapper;
    private final BinaryContentMapper binaryContentMapper;

    public MessageDto toDto(Message entity) {
        if (entity == null) {
            return null;
        }

        List<BinaryContentDto> attachmentDtos = entity.getAttachments().stream()
            .map(binaryContentMapper::toDto)
            .toList();

        return new MessageDto(
            entity.getId(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getContent(),
            entity.getChannel().getId(),
            userMapper.toDto(entity.getAuthor()),
            attachmentDtos
        );
    }
}
