package com.sprint.mission.discodeit.binarycontent.adapter.persistence;

import com.sprint.mission.discodeit.binarycontent.application.required.BinaryContentRepository;
import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BinaryContentRepositoryAdapter implements BinaryContentRepository {

  private final BinaryContentJpaRepository jpaRepository;

  @Override
  public BinaryContent save(BinaryContent binaryContent) {
    return jpaRepository.save(binaryContent);
  }

  @Override
  public Optional<BinaryContent> findById(UUID binaryContentId) {
    return jpaRepository.findById(binaryContentId);
  }

  @Override
  public List<BinaryContent> findAllById(List<UUID> binaryContentIds) {
    return jpaRepository.findAllById(binaryContentIds);
  }
}
