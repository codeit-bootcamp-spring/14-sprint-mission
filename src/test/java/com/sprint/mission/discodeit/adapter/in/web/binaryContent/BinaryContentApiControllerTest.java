package com.sprint.mission.discodeit.adapter.in.web.binaryContent;

import com.sprint.mission.discodeit.adapter.in.web.controller.binaryContent.BinaryContentApiController;
import com.sprint.mission.discodeit.core.application.binarycontent.port.out.dto.response.DownloadContent;
import com.sprint.mission.discodeit.core.application.binarycontent.port.in.BinaryContentUseCase;
import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.response.BinaryContentDownloadResult;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BinaryContentApiControllerTest {
    private final BinaryContentUseCase useCase = mock(BinaryContentUseCase.class);
    private final BinaryContentApiController controller = new BinaryContentApiController(useCase);

    @Test
    void localDownloadReturnsFileStreamWithContentType() throws Exception {
        UUID id = UUID.randomUUID();
        byte[] bytes = {1, 2, 3};
        when(useCase.downloadBinaryContent(id)).thenReturn(new BinaryContentDownloadResult(
                "image/png", new DownloadContent.Stream(new ByteArrayInputStream(bytes))));

        var response = controller.downloadBinaryContent(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().toString()).isEqualTo("image/png");
        assertThat(((InputStreamResource) response.getBody()).getInputStream().readAllBytes()).isEqualTo(bytes);
    }

    @Test
    void s3DownloadRedirectsToPresignedUrl() {
        UUID id = UUID.randomUUID();
        URI location = URI.create("https://example.com/signed");
        when(useCase.downloadBinaryContent(id)).thenReturn(new BinaryContentDownloadResult(
                "image/png", new DownloadContent.Redirect(location)));

        var response = controller.downloadBinaryContent(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getLocation()).isEqualTo(location);
        assertThat(response.getHeaders().getContentType().toString()).isEqualTo("image/png");
    }
}
