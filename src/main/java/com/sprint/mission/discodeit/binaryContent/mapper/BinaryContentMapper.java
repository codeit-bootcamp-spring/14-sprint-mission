package com.sprint.mission.discodeit.binaryContent.mapper;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentDto;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BinaryContentMapper {

    public BinaryContentDto toDto(BinaryContent binaryContent){
        return new BinaryContentDto(
                binaryContent.getId(),
                binaryContent.getFileName(),
                binaryContent.getSize(),
                binaryContent.getContentType()
        );
    }
}
