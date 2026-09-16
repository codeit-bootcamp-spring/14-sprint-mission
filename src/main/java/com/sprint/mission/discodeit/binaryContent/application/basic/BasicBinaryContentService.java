package com.sprint.mission.discodeit.binaryContent.application.basic;

import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.binaryContent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.binaryContent.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.binaryContent.application.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.UUID;

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
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
        return binaryContentMapper.toDto(binaryContent);

    }

    @Override
    @Transactional(readOnly = true)
    public BinaryContentDto find(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

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
            throw new NoSuchElementException();
        }

        return response;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(id).orElseThrow(NoSuchElementException::new);
        binaryContentRepository.deleteById(binaryContent.getId());
    }



}
