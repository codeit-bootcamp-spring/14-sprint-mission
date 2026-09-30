package com.sprint.mission.discodeit.binarycontent.domain.repository;

import com.sprint.mission.discodeit.binarycontent.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinaryContentRepository extends JpaRepository<BinaryContent, UUID> {
    default BinaryContent getByIdOrThrow(UUID binaryContentId) {
        return findById(binaryContentId).orElseThrow(() -> new CustomException(CustomErrorCode.FILE_NOT_FOUND));
    }
}
