package com.sprint.mission.discodeit.storage.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

class S3BinaryContentStorageTest {

    private static final Path ENV_PATH = Path.of(".env");

    private Properties properties;
    private S3BinaryContentStorage storage;
    private UUID binaryContentId;

    @BeforeEach
    void setUp() throws IOException {
        assumeTrue(Files.exists(ENV_PATH), ".env 파일이 없으면 실제 S3 테스트를 건너뜀");
        properties = new Properties();
        try (FileInputStream input = new FileInputStream(ENV_PATH.toFile())) {
            properties.load(input);
        }
        assumeTrue(properties.getProperty("AWS_S3_ACCESS_KEY") != null, "AWS 키가 없으면 건너뜀");

        storage = new S3BinaryContentStorage(
            properties.getProperty("AWS_S3_ACCESS_KEY"),
            properties.getProperty("AWS_S3_SECRET_KEY"),
            properties.getProperty("AWS_S3_REGION"),
            properties.getProperty("AWS_S3_BUCKET"));
        binaryContentId = UUID.randomUUID();
    }

    @AfterEach
    void tearDown() {
        if (storage == null) {
            return;
        }
        try (S3Client s3Client = S3Client.builder()
            .region(Region.of(properties.getProperty("AWS_S3_REGION")))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                properties.getProperty("AWS_S3_ACCESS_KEY"),
                properties.getProperty("AWS_S3_SECRET_KEY"))))
            .build()) {
            s3Client.deleteObject(builder -> builder
                .bucket(properties.getProperty("AWS_S3_BUCKET"))
                .key(binaryContentId.toString()));
        }
    }

    @Test
    void put으로_올린_파일을_get으로_읽을_수_있다() throws IOException {
        storage.put(binaryContentId, "hello-s3".getBytes(StandardCharsets.UTF_8));

        try (InputStream inputStream = storage.get(binaryContentId)) {
            assertThat(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8))
                .isEqualTo("hello-s3");
        }
    }

    @Test
    void 없는_파일을_get하면_예외가_발생한다() {
        assertThatThrownBy(() -> storage.get(binaryContentId))
            .isInstanceOf(BinaryContentNotFoundException.class);
    }

    @Test
    void download는_PresignedUrl로_리다이렉트한다() throws Exception {
        storage.put(binaryContentId, "redirect-test".getBytes(StandardCharsets.UTF_8));
        BinaryContentDto dto = new BinaryContentDto(binaryContentId, "test.txt", 13L, "text/plain");

        ResponseEntity<Void> response = storage.download(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        String location = response.getHeaders().getFirst(HttpHeaders.LOCATION);
        assertThat(location).contains("X-Amz-Signature");

        HttpResponse<String> fetched = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create(location)).GET().build(),
            HttpResponse.BodyHandlers.ofString());
        assertThat(fetched.statusCode()).isEqualTo(200);
        assertThat(fetched.body()).isEqualTo("redirect-test");
    }
}
