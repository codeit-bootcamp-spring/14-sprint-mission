package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.binarycontent.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.web.dto.res.BinaryContentResponseDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface BinaryContentMapper {
    BinaryContentResponseDTO toResponse(BinaryContent binaryContent);
}
