package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    public BinaryContent findById(UUID id) {
        return binaryContentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.BINARYCONTENT_NOT_FOUND_IN_DATABASE));
    }

    public List<BinaryContent> findAllById(List<UUID> ids) {
        return binaryContentRepository.findAllById(ids);
    }
}
