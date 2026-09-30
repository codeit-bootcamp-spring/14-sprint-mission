package com.sprint.mission.discodeit.binarycontent.application;

import com.sprint.mission.discodeit.binarycontent.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.binarycontent.domain.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.binarycontent.web.dto.res.BinaryContentResponseDTO;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Slf4j
@Service
public class BinaryApplicationService {
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage fileStorage;
    private final BinaryContentMapper binaryContentMapper;

    @Transactional
    public BinaryContent storeMultipartFile(MultipartFile multipartFile) {

        BinaryContent binaryContent = BinaryContent.init(
            multipartFile.getOriginalFilename(),
            multipartFile.getSize(),
            multipartFile.getContentType()
        );

        BinaryContent saved = binaryContentRepository.save(binaryContent);

        try{
            fileStorage.put(saved.getId(), multipartFile.getBytes());
        }catch (IOException e){
            log.error("바이트 데이터 추출 실패", e);
            throw new CustomException(CustomErrorCode.FILE_STORE_FAILED);
        }

        return saved;
    }

    // 이거 트랜잭셔널 안걸리는데 - 진입점이 files -> file todo
    @Transactional
    public List<BinaryContent> storeMultipartFiles(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream()
            .map(this::storeMultipartFile)
            .toList();
    }

    public BinaryContentResponseDTO findStoreFile(UUID binaryContentId) {
        return binaryContentMapper.toResponse(binaryContentRepository.getByIdOrThrow(binaryContentId));
    }

    public List<BinaryContentResponseDTO> findAllStoreFileByIdIn(List<UUID> fileIdList) {
        // 단일 조회/단순 존재 검증만 default
        List<BinaryContent> binaryContents = binaryContentRepository.findAllById(fileIdList);

        if (binaryContents.size() != fileIdList.size()) {
            throw new CustomException(CustomErrorCode.FILE_NOT_FOUND);
        }

        return binaryContents.stream()
            .map(binaryContentMapper::toResponse)
            .toList();
    }

    @Transactional
    public BinaryContentDownload download(UUID fileId){
        BinaryContent binaryContent = binaryContentRepository.getByIdOrThrow(fileId);

        Resource resource = new InputStreamResource(
            fileStorage.get(binaryContent.getId())
        );

        return BinaryContentDownload.of(binaryContent, resource);
    }
}
