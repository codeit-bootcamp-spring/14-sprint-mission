package com.sprint.mission.discodeit.binaryContent.application.basic;

import com.sprint.mission.discodeit.binaryContent.application.BinaryContentService;
import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binaryContent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.binaryContent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.common.exception.BinaryContentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;

    @Override
    @Transactional
    public BinaryContentDto create(BinaryContentCreateRequestDto request) {
        MultipartFile file = request.data();

        BinaryContent binaryContent = new BinaryContent(file.getOriginalFilename(), file.getSize(), file.getContentType());

        binaryContentRepository.save(binaryContent);
        try {
            binaryContentStorage.put(binaryContent.getId(), file.getBytes());
        } catch (IOException e) {
            log.error("바이너리 컨텐츠 생성 실패 - fileName = {}", binaryContent.getFileName(), e);
            throw new UncheckedIOException(e);
        }
        log.info("바이너리 컨텐츠 생성 성공 - fileName = {}", binaryContent.getFileName());
        return binaryContentMapper.toDto(binaryContent);

    }

    @Override
    @Transactional(readOnly = true)
    public BinaryContentDto find(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("바이너리 컨텐츠 찾기 실패 - id = {}", id);
                    return new BinaryContentNotFoundException(id);
                });

        log.debug("바이너리 컨텐츠 찾기 성공 - id = {}", id);
        return binaryContentMapper.toDto(binaryContent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentDto> findAllByIdIn(List<UUID> ids) {
        List<BinaryContentDto> response = binaryContentRepository.findAllByIdIn(ids)
                .stream()
                .map(binaryContentMapper::toDto)
                .toList();

        // 비어 있으면 예외 발생
        if (response.isEmpty()) {
            log.warn("바이너리 컨텐츠 찾기 실패 - ids = {}", ids);
            throw new BinaryContentNotFoundException(ids);
        }

        log.debug("바이너리 컨텐츠 찾기 성공 - ids = {}", ids);
        return response;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("바이너리 컨텐츠 찾기 실패(삭제) - id = {}", id);
                    return new BinaryContentNotFoundException(id);
                });

        binaryContentRepository.deleteById(binaryContent.getId());
        log.info("바이너리 컨텐츠 삭제 성공 - id = {}", id);
    }


}
