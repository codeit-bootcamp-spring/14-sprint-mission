package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;


@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFReadStatusRepository extends JCFRepository<ReadStatus>
implements ReadStatusRepository {

    public JCFReadStatusRepository() {
        super();
    }


    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return findAll().stream().filter(readStatus ->
                readStatus.getUserId().equals(userId))
            .toList();
    }


    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return findAll().stream()
            .filter(readStatus -> readStatus.getChannelId().equals(channelId))
            .toList();
    }
}
