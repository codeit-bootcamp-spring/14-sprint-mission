package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentApplication {
    private final BinaryContentRepository binaryContentRepository;

    public BinaryContent create(BinaryContent binaryContent) {
        return binaryContentRepository.create(binaryContent);
    }

    public BinaryContent getBinaryContent(UUID id) {
        return binaryContentRepository.findById(id).orElseThrow();
    }

    public List<BinaryContentResponseDto> getAllBinaryContents(List<UUID> ids) {
        return binaryContentRepository.findAllById(ids).stream()
                .map(BinaryContentResponseDto::of)
                .toList();
    }

    public void delete(UUID id) {
        binaryContentRepository.deleteById(id);
    }
}
