package com.sprint.mission.application.binarycontent;

import com.sprint.mission.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.dto.binarycontent.BinaryContentResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BinaryContentApplicationService {
    BinaryContentResponseDto create(MultipartFile multipartFile);
    BinaryContentResponseDto findById(UUID binaryContentId);
    List<BinaryContentResponseDto> findAllByIdIn(List<UUID> binaryContentIds);
    void delete(UUID binaryContentId);
}
