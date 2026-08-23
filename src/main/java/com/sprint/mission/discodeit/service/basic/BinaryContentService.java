package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.IService.IBinaryContentService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BinaryContentService implements IBinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentResponseDto create(BinaryContentCreateRequestDto request) {
        BinaryContent binaryContent = request.toEntity();
        binaryContentRepository.save(binaryContent);
        return BinaryContentResponseDto.from(binaryContent);
    }

    @Override
    public BinaryContentResponseDto find(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id);
        if (Objects.isNull(binaryContent)) {
            throw new RuntimeException("데이터가 없습니다.");
        }

        return BinaryContentResponseDto.from(binaryContent);
    }



    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> ids) {
        List<BinaryContent> binarycontents = binaryContentRepository.findAllByIdIn(ids);

        return binarycontents.stream()
            .map(BinaryContentResponseDto::from)
            .toList();
    }

    @Override
    public void delete(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id);
        if (Objects.isNull(binaryContent)) {
            throw new RuntimeException("존재하지 않는 파일입니다");
        }
       binaryContentRepository.deleteById(id);

    }
}
