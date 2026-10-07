package com.sprint.mission.discodeit.binarycontent.application.mapper;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BinaryContentMapper {

  public List<BinaryContentDto> toList(List<BinaryContent> binaryContents) {
    if (binaryContents == null) {
      return List.of();
    }
    return binaryContents.stream().map(this::toDto).toList();
  }

  public BinaryContentDto toDto(BinaryContent binaryContent) {
    if (binaryContent == null) {
      return null;
    }
    return new BinaryContentDto(binaryContent.getId(), binaryContent.getFileName(),
        binaryContent.getSize(),
        binaryContent.getContentType());
  }
}
