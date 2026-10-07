package com.sprint.mission.discodeit.binarycontent.application;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binarycontent.application.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.binarycontent.application.provided.query.BinaryContentFinder;
import com.sprint.mission.discodeit.binarycontent.application.required.BinaryContentRepository;
import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
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
        .orElseThrow(() -> new DiscodeitRuntimeException(ErrorCode.BINARY_CONTENT_NOT_FOUND));
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
