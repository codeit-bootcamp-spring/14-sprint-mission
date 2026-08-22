package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentApplication {
    private final BinaryContentService binaryContentService;

    public BinaryContent create(BinaryContent binaryContent) {
        return binaryContentService.create(binaryContent);
    }

    public BinaryContentResponseDto getBinaryContent(UUID id) {
        return BinaryContentResponseDto.of(binaryContentService.findById(id));
    }

    public List<BinaryContentResponseDto> getAllBinaryContents(List<UUID> ids) {
        return binaryContentService.findAllById(ids).stream()
                .map(BinaryContentResponseDto::of)
                .toList();
    }

    public void delete(UUID id) {
        binaryContentService.deleteById(id);
    }
}
