package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    final List<BinaryContent> binaryContentList;

    public JCFBinaryContentRepository() {
        this.binaryContentList = new ArrayList<>();
    }

    @Override
    public void save(BinaryContent binaryContent) {
        if (findById(binaryContent.getId()) != null) {
            return;
        }
        binaryContentList.add(binaryContent);
    }

    @Override
    public BinaryContent findById(UUID id) {
        for (BinaryContent each : binaryContentList) {
            if (each.getId().equals(id)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public BinaryContent findByUserId(UUID userId) {
        for (BinaryContent each : binaryContentList) {
            if (each.getUserId() != null && each.getUserId().equals(userId)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public List<BinaryContent> findAllByMessageId(UUID messageId) {
        return binaryContentList.stream()
                .filter(each -> each.getMessageId() != null && each.getMessageId().equals(messageId))
                .toList();
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
