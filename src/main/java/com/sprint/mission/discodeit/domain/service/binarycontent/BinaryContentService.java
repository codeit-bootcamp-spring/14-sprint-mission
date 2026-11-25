package com.sprint.mission.discodeit.domain.service.binarycontent;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentService {
    List<BinaryContent> storeFiles(List<MultipartFile> multipartFiles);
    BinaryContent storeFile(MultipartFile multipartFile);
    BinaryContent findStoreFile(UUID binaryContentUUID);
    List<BinaryContent> findAllStoreFileByIdIn(List<UUID> fileIdList);
    void deleteStoreFileById(UUID binaryContentUUID);
    List<BinaryContent> findAllStoreFile();
}
