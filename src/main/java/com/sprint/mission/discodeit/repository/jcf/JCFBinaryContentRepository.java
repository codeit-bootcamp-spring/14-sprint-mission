package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    final List<BinaryContent> binaryContentList;

    public JCFBinaryContentRepository() {
        this.binaryContentList = new ArrayList<>();
    }

    @Override
    public void save(BinaryContent binaryContent) {
        binaryContentList.add(binaryContent);
    }

    @Override
    public BinaryContent findById(UUID id) {
        return binaryContentList.stream()
                .filter(each -> each.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public BinaryContent findByUserId(UUID userId) {
        return binaryContentList.stream()
                .filter(each -> each.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public BinaryContent findByUserIdAndChannelId(UUID userId, UUID channelId) {
        for (BinaryContent each : binaryContentList) {
            if (each.getUserId() != null && each.getUserId().equals(userId)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public List<BinaryContent> findAll() {
        return this.binaryContentList;
    }

    @Override
    public void delete(UUID id) {
        BinaryContent target = findById(id);
        if (target != null) {
            binaryContentList.remove(target);
        }
    }
}
