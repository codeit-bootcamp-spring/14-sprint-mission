package com.sprint.mission.discodeit.binaryContent.repository.jcf;

import com.sprint.mission.discodeit.binaryContent.domain.BinaryContent;
import com.sprint.mission.discodeit.binaryContent.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> data;

    public JCFBinaryContentRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public void save(BinaryContent binaryContent) {
        data.put(binaryContent.getId(), binaryContent);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<BinaryContent> findAll() {
        return data.values().stream()
                .toList();
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return findAll().stream()
                .filter(binaryContent -> ids.contains(binaryContent.getId()))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
}
