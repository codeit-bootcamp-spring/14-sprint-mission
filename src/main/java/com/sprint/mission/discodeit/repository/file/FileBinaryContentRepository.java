package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository extends FileRepository<BinaryContent>
    implements BinaryContentRepository {

    public FileBinaryContentRepository(
        @Value("${discodeit.repository.file-directory:.discodeit}") String fileDirectory
    ) {
        super(fileDirectory,"binaryContent");
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return findAll().stream()
            .filter(binaryContent -> ids.contains(binaryContent.getId()))
            .toList();
    }


}
