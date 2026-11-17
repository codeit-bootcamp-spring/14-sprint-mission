package com.sprint.mission.discodeit.binaryContent.storage;

import static org.assertj.core.api.Assertions.*;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sprint.mission.discodeit.binaryContent.dto.BinaryContentDto;
import com.sprint.mission.discodeit.common.exception.BinaryContentNotFoundException;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

// 실제 S3 버킷에 붙는 테스트라 .env가 있을 때만 실행
@EnabledIf("envFileExists")
class S3BinaryContentStorageTest {

    private static final Path ENV_PATH = Path.of(".env");
    private static Properties props;

    private S3BinaryContentStorage storage;
    private S3Client cleanupClient;   // 테스트로 올린 파일을 지우기 위한 클라이언트
    private String bucket;
    private UUID binaryContentId;

    static boolean envFileExists() {
        return Files.exists(ENV_PATH);
    }

    @BeforeAll
    static void loadEnv() throws Exception {
        props = new Properties();
        try (InputStream in = Files.newInputStream(ENV_PATH)) {
            props.load(in);
        }
    }

    @BeforeEach
    void setUp() {
        String accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
        String secretKey = props.getProperty("AWS_S3_SECRET_KEY");
        String region = props.getProperty("AWS_S3_REGION");
        bucket = props.getProperty("AWS_S3_BUCKET");

        // 스프링 없이 직접 생성 (생성자에 yaml 대신 .env 값을 넣음)
        storage = new S3BinaryContentStorage(accessKey, secretKey, region, bucket, 600);

        cleanupClient = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)))
            .build();

        binaryContentId = UUID.randomUUID();
    }

    @AfterEach
    void tearDown() {
        cleanupClient.deleteObject(r -> r.bucket(bucket).key(binaryContentId.toString()));
        cleanupClient.close();
    }

    @Test
    @DisplayName("put 후 get - 올린 파일을 그대로 읽는다")
    void putAndGet_success() throws Exception {
        byte[] content = "hello discodeit".getBytes(StandardCharsets.UTF_8);

        UUID result = storage.put(binaryContentId, content);

        assertThat(result).isEqualTo(binaryContentId);
        try (InputStream in = storage.get(binaryContentId)) {
            assertThat(in.readAllBytes()).isEqualTo(content);
        }
    }

    @Test
    @DisplayName("get 실패 - 없는 파일이면 BinaryContentNotFoundException")
    void get_notFound_fail() {
        assertThatThrownBy(() -> storage.get(binaryContentId))
            .isInstanceOf(BinaryContentNotFoundException.class);
    }

    @Test
    @DisplayName("download - Presigned URL로 302 리다이렉트하고, 그 링크로 파일을 받을 수 있다")
    void download_redirectsToPresignedUrl() throws Exception {
        byte[] content = "hello discodeit".getBytes(StandardCharsets.UTF_8);
        storage.put(binaryContentId, content);
        BinaryContentDto dto = new BinaryContentDto(
            binaryContentId, "테스트 파일.txt", (long)content.length, "text/plain");

        ResponseEntity<?> response = storage.download(dto);

        // 1. 302 + Location 헤더에 서명된 S3 주소가 들어 있는지
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        URI location = response.getHeaders().getLocation();
        assertThat(location).isNotNull();
        assertThat(location.toString())
            .contains(bucket)
            .contains(binaryContentId.toString())
            .contains("X-Amz-Signature");

        // 2. 그 링크로 실제 다운로드가 되는지 (브라우저가 리다이렉트를 따라가는 것과 같음)
        HttpResponse<byte[]> s3Response = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(location).GET().build(),
            HttpResponse.BodyHandlers.ofByteArray());
        assertThat(s3Response.statusCode()).isEqualTo(200);
        assertThat(s3Response.body()).isEqualTo(content);
        assertThat(s3Response.headers().firstValue("Content-Disposition").orElse(""))
            .contains("attachment");
    }
}