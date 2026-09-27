package com.sprint.mission.discodeit.application.binarycontent.mapper;

import com.sprint.mission.discodeit.application.binarycontent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.domain.BinaryContent;
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
