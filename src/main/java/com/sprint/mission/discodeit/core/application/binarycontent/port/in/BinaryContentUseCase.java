package com.sprint.mission.discodeit.core.application.binarycontent.port.in;

import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.response.BinaryContentDownloadResult;
import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.response.BinaryContentResult;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface BinaryContentUseCase {
    BinaryContentResult getBinaryContentInfo(@NotNull UUID id);

    List<BinaryContentResult> getAllBinaryContentInfos(@NotNull List<@NotNull UUID> ids);

    BinaryContentDownloadResult downloadBinaryContent(@NotNull UUID id);
}
