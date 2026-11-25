package com.sprint.mission.discodeit.application.binarycontent;

import com.sprint.mission.discodeit.core.application.binarycontent.port.out.BinaryContentPort;
import com.sprint.mission.discodeit.core.application.binarycontent.port.out.BinaryContentStoragePort;
import com.sprint.mission.discodeit.core.application.binarycontent.service.BinaryContentApplication;
import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContentException;
import com.sprint.mission.discodeit.core.domain.binaryContent.BinaryContentExceptionType;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class BinaryContentApplicationTest {
    private final BinaryContentPort contents = mock(BinaryContentPort.class);
    private final BinaryContentStoragePort storage = mock(BinaryContentStoragePort.class);
    private final BinaryContentApplication application = new BinaryContentApplication(contents, storage);

    @Test
    void missingMetadataStopsDownloadBeforeStorageAccess() {
        UUID id = UUID.randomUUID();
        when(contents.findById(id)).thenReturn(Optional.empty());
        doCallRealMethod().when(contents).get(id);

        assertThatThrownBy(() -> application.downloadBinaryContent(id))
                .isInstanceOfSatisfying(BinaryContentException.class, exception -> {
                    assertThat(exception.getType()).isEqualTo(BinaryContentExceptionType.BINARY_CONTENT_NOT_FOUND);
                    assertThat(exception.getDetails()).containsEntry("id", id);
                });
        verifyNoInteractions(storage);
    }
}
