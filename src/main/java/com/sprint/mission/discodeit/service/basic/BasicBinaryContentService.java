package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontentdto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontentdto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.CustomRuntimeException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentResponseDto createBinaryContent(BinaryContentCreateRequestDto requestDto) {
        BinaryContent binaryContent = requestDto.toEntity();
        binaryContentRepository.save(binaryContent);
        return BinaryContentResponseDto.from(binaryContent);
    }

    @Override
    public BinaryContentResponseDto readBinaryContent(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id);
        if (Objects.isNull(binaryContent)) {
            // throw new RuntimeException("해당 BinaryContent가 존재하지 않습니다:");
            throw new CustomRuntimeException(ExceptionType.BINARY_CONTENT_NOT_FOUND, id);
        }
        return BinaryContentResponseDto.from(binaryContent);
    }

    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return binaryContentRepository.findAll().stream()
                .filter(each -> ids.contains(each.getId()))
                .map(each -> BinaryContentResponseDto.from(each))
                .toList();
    }

    @Override
    public void deleteBinaryContent(UUID id) {
        binaryContentRepository.delete(id);
    }
}
