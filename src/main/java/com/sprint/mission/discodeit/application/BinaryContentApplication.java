package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.common.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentApplication {
    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    public BinaryContentResponseDto getBinaryContentInfo(UUID id) {
        return BinaryContentResponseDto.from(binaryContentService.findById(id));
    }

    public List<BinaryContentResponseDto> getAllBinaryContentInfos(List<UUID> ids) {
        return binaryContentService.findAllById(ids).stream()
                .map(BinaryContentResponseDto::from)
                .toList();
    }

    public ResponseEntity<?> getBinaryContent(UUID id) {
        BinaryContentResponseDto response = BinaryContentResponseDto.from(binaryContentService.findById(id));
        return binaryContentStorage.download(response);
    }
}
