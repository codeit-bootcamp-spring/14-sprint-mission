package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFBinaryContentRepository implements BinaryContentRepository {

    protected Map<UUID, BinaryContent> data;

    @Override
    public void save(BinaryContent binaryContent) {
        data.put(binaryContent.getId(), binaryContent);
    }

    @Override
    public BinaryContent findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return data.values().stream()
            .filter(binaryContent -> ids.contains(binaryContent.getId()))
            .toList();
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
}
