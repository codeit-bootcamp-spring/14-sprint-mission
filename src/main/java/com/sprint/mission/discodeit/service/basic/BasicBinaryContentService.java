package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService{
    private final BinaryContentRepository binaryContentRepository;

    public BinaryContent create(BinaryContent binaryContent) {
        return binaryContentRepository.create(binaryContent);
    }

    public BinaryContent getBinaryContent(UUID id) {
        return binaryContentRepository.findById(id).orElseThrow();
    }

    public List<BinaryContent> getAllBinaryContents(List<UUID> ids) {
        return binaryContentRepository.findAllById(ids);
    }

    public void delete(UUID id) {
        binaryContentRepository.deleteById(id);
    }
}
