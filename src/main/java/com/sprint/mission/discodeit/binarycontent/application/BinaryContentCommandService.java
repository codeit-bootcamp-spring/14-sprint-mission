package com.sprint.mission.discodeit.binarycontent.application;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.application.provided.command.BinaryContentRegister;
import com.sprint.mission.discodeit.binarycontent.application.required.BinaryContentRepository;
import com.sprint.mission.discodeit.binarycontent.application.required.BinaryContentStorage;
import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BinaryContentCommandService implements BinaryContentRegister {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  public List<BinaryContent> register(List<BinaryContentCreateRequest> attachments) {
    if (attachments == null || attachments.isEmpty()) {
      return List.of();
    }
    List<BinaryContent> binaryContents = new ArrayList<>();

    for (BinaryContentCreateRequest request : attachments) {
      BinaryContent binaryContent =
          BinaryContent.create(
              request.fileName(),
              request.size(),
              request.contentType()
          );
      BinaryContent saved = binaryContentRepository.save(binaryContent);
      binaryContentStorage.put(saved.getId(), request.bytes());
      binaryContents.add(saved);
    }
    return binaryContents;
  }


}
