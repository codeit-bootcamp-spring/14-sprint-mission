package com.sprint.mission.discodeit.binarycontent.repository;

import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> binaryContentMap = new HashMap<>();

    @Override
    public BinaryContent binaryAdd(BinaryContent binaryContent) {
        binaryContentMap.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    @Override
    public void delete(UUID binaryId) {
        binaryContentMap.remove(binaryId);
    }

    @Override
    public BinaryContent findById(UUID binaryId) {
        return binaryContentMap.get(binaryId);
    }

    @Override
    public List<BinaryContent> findAll() {
        return binaryContentMap.values().stream()
            .toList();
    }

    @Override
    public BinaryContent toBinaryContent(MultipartFile file) {
        try {
            BinaryContent binaryContent = new BinaryContent(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
            );
            return binaryAdd(binaryContent);
        } catch (IOException e) {
            throw new UncheckedIOException("파일을 읽는데 실패했습니다: " + file.getOriginalFilename(), e);
        }
    }
}
