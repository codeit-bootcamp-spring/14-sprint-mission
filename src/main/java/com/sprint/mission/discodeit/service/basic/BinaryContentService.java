package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.IService.IBinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BinaryContentService implements IBinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    public BinaryContentResponseDto create(BinaryContentCreateRequestDto request) {
        BinaryContent binaryContent = request.toEntity();
        binaryContentRepository.save(binaryContent);
        binaryContentStorage.put(binaryContent.getId(), request.bytes());
        return BinaryContentResponseDto.from(binaryContent);
    }

    @Override
    @Transactional(readOnly = true)
    public BinaryContentResponseDto find(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("데이터가 없습니다."));

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
        BinaryContent binaryContent = binaryContentRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 파일입니다."));

        binaryContentRepository.deleteById(id);
    }
}
