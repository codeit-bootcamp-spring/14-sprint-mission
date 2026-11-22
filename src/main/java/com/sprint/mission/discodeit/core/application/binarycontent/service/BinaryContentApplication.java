package com.sprint.mission.discodeit.core.application.binarycontent.service;

import com.sprint.mission.discodeit.core.application.binarycontent.port.in.BinaryContentUseCase;
import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.response.BinaryContentDownloadResult;
import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.response.BinaryContentResult;
import com.sprint.mission.discodeit.core.application.binarycontent.port.out.BinaryContentPort;
import com.sprint.mission.discodeit.core.application.binarycontent.port.out.BinaryContentStoragePort;
import com.sprint.mission.discodeit.core.common.annotation.ApplicationService;
import com.sprint.mission.discodeit.core.common.log.MutationLog;
import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContentException;
import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContentExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationService
@RequiredArgsConstructor
public class BinaryContentApplication implements BinaryContentUseCase {
    private final BinaryContentPort binaryContents;
    private final BinaryContentStoragePort storage;

    @Override
    @Transactional(readOnly = true)
    public BinaryContentResult getBinaryContentInfo(UUID id) {
        return BinaryContentResult.from(binaryContents.get(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentResult> getAllBinaryContentInfos(List<UUID> ids) {
        return binaryContents.findAllById(ids).stream()
                .map(BinaryContentResult::from)
                .toList();
    }

    @Override
    @MutationLog
    @Transactional(readOnly = true)
    public BinaryContentDownloadResult downloadBinaryContent(UUID id) {
        BinaryContent content = binaryContents.findById(id)
                .orElseThrow(() -> new BinaryContentException(
                        BinaryContentExceptionType.BINARY_CONTENT_NOT_FOUND, Map.of("id", id)));
        return new BinaryContentDownloadResult(content.getContentType(), storage.download(id));
    }
}
