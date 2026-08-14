package com.sprint.mission.discodeit.service.application.binarycontent;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.domain.binarycontent.BinaryContentDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinaryContentApplicationServiceImpl implements BinaryContentApplicationService {

    private final BinaryContentDomainService binaryContentDomainService;

    @Override
    public BinaryContentResponseDto create(MultipartFile multipartFile) {
        BinaryContent binaryContent = BinaryContent.create(multipartFile);

        BinaryContent createdBinaryContent = binaryContentDomainService.create(binaryContent);

        log.info(
                "BinaryContent 생성 완료: binaryContentId={}, fileName={}, size={}",
                createdBinaryContent.getId(),
                createdBinaryContent.getFileName(),
                createdBinaryContent.getBytes().length
        );

        return BinaryContentResponseDto.from(createdBinaryContent);
    }

    @Override
    public BinaryContentResponseDto findById(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentDomainService.findById(binaryContentId);
        log.debug(
                "BinaryContent 단건 조회: binaryContentId={}",
                binaryContentId
        );
        return BinaryContentResponseDto.from(binaryContent);
    }

    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> binaryContentIds) {
        List<BinaryContentResponseDto> responses =
                binaryContentDomainService.findAllByIdIn(binaryContentIds)
                        .stream()
                        .map(BinaryContentResponseDto::from)
                        .toList();

        log.debug(
                "BinaryContent 목록 조회 완료: requestedCount={}, resultCount={}",
                binaryContentIds.size(),
                responses.size()
        );

        return responses;
    }

    @Override
    public void delete(UUID binaryContentId) {
        log.info(
                "BinaryContent 삭제 시작: binaryContentId={}",
                binaryContentId
        );

        binaryContentDomainService.delete(binaryContentId);

        log.info(
                "BinaryContent 삭제 완료: binaryContentId={}",
                binaryContentId
        );
    }
}
