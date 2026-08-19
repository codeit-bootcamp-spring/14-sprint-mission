package com.sprint.mission.discodeit.binarycontent.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BinaryContentServiceImpl implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentResponseDto binaryContentCreate(
        BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        return BinaryContentResponseDto.from(binaryContentRepository.binaryAdd(
            new BinaryContent(binaryContentCreateRequestDto.fileName(),
                binaryContentCreateRequestDto.contentType(),
                binaryContentCreateRequestDto.bytes())));
    }

    @Override
    public void binaryContentDelete(UUID binaryContentId) {
        binaryContentRepository.delete(binaryContentId);
    }

    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> binaryContentIds) {
        List<BinaryContent> binaryContents = binaryContentRepository.findAllByIdIn(
            binaryContentIds);

        return binaryContents.stream()
            .map(BinaryContentResponseDto::from)
            .toList();
    }

    @Override
    public BinaryContentResponseDto findBinaryContent(UUID binaryContentId) {
        return BinaryContentResponseDto.from(binaryContentRepository.findById(binaryContentId));
    }
}
