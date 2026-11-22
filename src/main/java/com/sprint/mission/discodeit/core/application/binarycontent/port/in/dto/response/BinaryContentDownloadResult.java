package com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.response;

import com.sprint.mission.discodeit.core.application.binarycontent.port.out.dto.response.DownloadContent;

public record BinaryContentDownloadResult(String contentType, DownloadContent content) {
}
