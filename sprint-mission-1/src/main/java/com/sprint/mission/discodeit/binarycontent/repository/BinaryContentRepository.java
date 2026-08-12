package com.sprint.mission.discodeit.binarycontent.repository;

import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface BinaryContentRepository {

    BinaryContent binaryAdd(BinaryContent binaryContent);

    void delete(UUID binaryId);

    public BinaryContent findById(UUID binaryId);

    public List<BinaryContent> findAll();

    BinaryContent toBinaryContent(MultipartFile file);
}
