package com.sprint.mission.discodeit.application.binarycontent;

import com.sprint.mission.discodeit.application.binarycontent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.application.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.application.binarycontent.provided.query.BinaryContentFinder;
import com.sprint.mission.discodeit.application.binarycontent.required.BinaryContentRepository;
import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.BinaryContent;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BinaryContentQueryService implements BinaryContentFinder {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  public BinaryContentDto getById(UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new DiscodeitRuntimeException(ExceptionType.BINARY_CONTENT_NOT_FOUND));
    return binaryContentMapper.toDto(binaryContent);
  }

  @Override
  public List<BinaryContentDto> getAllByIds(List<UUID> binaryContentIds) {
    if (binaryContentIds == null || binaryContentIds.isEmpty()) {
      return List.of();
    }
    return binaryContentRepository.findAllById(binaryContentIds).stream()
        .map(binaryContentMapper::toDto).toList();
  }
}
