package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf"
)
public class JCFBinaryContentRepository extends AbstractJCFRepository<BinaryContent>
        implements BinaryContentRepository {
    @Override
    public void delete(List<UUID> ids) {
        for (UUID id : ids) {
            super.deleteById(id);
        }
    }

    @Override
    public List<BinaryContent> findAllById(List<UUID> ids) {
        return findAll().stream()
                .filter(binaryContent -> ids.contains(binaryContent.getId()))
                .toList();
    }
}
