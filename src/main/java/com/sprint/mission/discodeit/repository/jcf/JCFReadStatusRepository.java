package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    final List<ReadStatus> readStatusList;

    public JCFReadStatusRepository() {
        this.readStatusList = new ArrayList<>();
    }

    @Override
    public void save(ReadStatus readStatus) {
        readStatusList.add(readStatus);
    }

    @Override
    public ReadStatus findById(UUID id) {
        for (ReadStatus each : readStatusList) {
            if (each.getId().equals(id)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public List<ReadStatus> findAll() {
        return readStatusList;
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusList.stream()
                .filter(r -> r.getChannelId() != null && r.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void delete(UUID id) {
        ReadStatus target = findById(id);
        if (target != null) {
            readStatusList.remove(target);
        }
    }
}
