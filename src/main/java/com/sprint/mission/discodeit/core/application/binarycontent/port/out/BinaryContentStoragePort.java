package com.sprint.mission.discodeit.core.application.binarycontent.port.out;

import com.sprint.mission.discodeit.core.application.binarycontent.port.out.dto.response.DownloadContent;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStoragePort {
    UUID put(UUID id, byte[] bytes);

    InputStream get(UUID id);

    DownloadContent download(UUID id);
}
