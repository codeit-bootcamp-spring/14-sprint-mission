package com.sprint.mission.discodeit.binarycontent.service;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BinaryContentServiceImpl implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    public BinaryContentServiceImpl(BinaryContentRepository binaryContentRepository) {
        this.binaryContentRepository = binaryContentRepository;
    }

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
    public List<BinaryContentResponseDto> findAllByIdIn(UUID userId) {
        List<BinaryContent> binaryContents = binaryContentRepository.findAllByIdIn();

        return binaryContents.stream()
            .map(BinaryContentResponseDto::from)
            .toList();
    }

    @Override
    public BinaryContentResponseDto findBinaryContent(UUID binaryContentId) {
        return BinaryContentResponseDto.from(binaryContentRepository.findById(binaryContentId));
    }
}
