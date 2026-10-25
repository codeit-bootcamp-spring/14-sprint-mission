package com.sprint.mission.discodeit.storage.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.AbortableInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@ExtendWith(MockitoExtension.class)
class S3BinaryContentStorageTest {

    @Mock
    private S3Client s3Client;

    private S3BinaryContentStorage storage;

    @BeforeEach
    void setUp() {
        storage = spy(new S3BinaryContentStorage("access", "secret", "ap-northeast-2", "bucket"));
    }

    @Test
    @DisplayName("파일 업로드 성공 시 UUID를 반환한다")
    void put_Success() {
        UUID id = UUID.randomUUID();
        byte[] bytes = "test".getBytes();

        doReturn(s3Client).when(storage).getS3Client();
        given(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
            .willReturn(PutObjectResponse.builder().build());

        UUID result = storage.put(id, bytes);

        assertThat(result).isEqualTo(id);
        verify(s3Client).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    @DisplayName("파일 조회 성공 시 InputStream을 반환한다")
    void get_Success() throws Exception {
        UUID id = UUID.randomUUID();
        byte[] bytes = "test".getBytes();

        ResponseInputStream<GetObjectResponse> mockResponse = new ResponseInputStream<>(
            GetObjectResponse.builder().build(),
            AbortableInputStream.create(new ByteArrayInputStream(bytes))
        );

        doReturn(s3Client).when(storage).getS3Client();
        given(s3Client.getObject(any(GetObjectRequest.class))).willReturn(mockResponse);

        InputStream result = storage.get(id);

        assertThat(result.readAllBytes()).isEqualTo(bytes);
    }

    @Test
    @DisplayName("S3에 키가 없으면 NoSuchElementException을 던진다")
    void get_NotFound() {
        UUID id = UUID.randomUUID();

        doReturn(s3Client).when(storage).getS3Client();
        given(s3Client.getObject(any(GetObjectRequest.class)))
            .willThrow(NoSuchKeyException.builder().build());

        assertThatThrownBy(() -> storage.get(id))
            .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("download 호출 시 Presigned URL 리다이렉트 응답을 반환한다")
    void download_ReturnsRedirect() {
        UUID id = UUID.randomUUID();
        BinaryContentDto dto = new BinaryContentDto(id, "test.png", 1024L, "image/png");
        String fakeUrl = "https://s3.amazon.com/" + id;

        doReturn(fakeUrl).when(storage).generatePresignedUrl(id.toString(), "image/png");

        ResponseEntity<?> response = storage.download(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        assertThat(response.getHeaders().getFirst(HttpHeaders.LOCATION)).isEqualTo(fakeUrl);
    }
}