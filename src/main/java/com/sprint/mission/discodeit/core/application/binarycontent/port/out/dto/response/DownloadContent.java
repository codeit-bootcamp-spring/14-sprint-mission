package com.sprint.mission.discodeit.core.application.binarycontent.port.out.dto.response;

import java.io.InputStream;
import java.net.URI;

public sealed interface DownloadContent
        permits DownloadContent.Stream, DownloadContent.Redirect {

    // Local
    record Stream(InputStream inputStream) implements DownloadContent {
    }

    // S3
    record Redirect(URI location) implements DownloadContent {
    }
}
