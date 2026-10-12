package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;

    @Transactional
    @Override
    public BinaryContentDto create(BinaryContentCreateRequest request) {
        BinaryContent binaryContent = new BinaryContent(
            request.fileName(), (long) request.bytes().length, request.contentType());
        binaryContentRepository.save(binaryContent);
        binaryContentStorage.put(binaryContent.getId(), request.bytes());
        log.info("파일 정보 저장 완료: binaryContentId={}, fileName={}", binaryContent.getId(),
            binaryContent.getFileName());
        return binaryContentMapper.toDto(binaryContent);
    }

    @Transactional(readOnly = true)
    @Override
    public BinaryContentDto find(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
            .map(binaryContentMapper::toDto)
            .orElseThrow(() -> new BinaryContentNotFoundException(binaryContentId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllById(binaryContentIds).stream()
            .map(binaryContentMapper::toDto)
            .toList();
    }

    @Transactional
    @Override
    public void delete(UUID binaryContentId) {
        if (!binaryContentRepository.existsById(binaryContentId)) {
            throw new BinaryContentNotFoundException(binaryContentId);
        }
        binaryContentRepository.deleteById(binaryContentId);
        log.info("파일 정보 삭제 완료: binaryContentId={}", binaryContentId);
    }
}
