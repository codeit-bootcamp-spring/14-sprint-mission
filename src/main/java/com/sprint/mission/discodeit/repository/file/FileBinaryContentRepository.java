package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

@Repository
public class FileBinaryContentRepository extends AbstractFileRepository<BinaryContent>
        implements BinaryContentRepository {

    protected FileBinaryContentRepository() {
        super(Files.BINARY_CONTENT);
    }

}
