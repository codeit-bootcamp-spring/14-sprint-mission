package com.sprint.mission.discodeit.storage.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

class AWSS3Test {

    private static final Path ENV_PATH = Path.of(".env");

    private Properties properties;
    private S3Client s3Client;
    private String bucket;
    private String key;

    @BeforeEach
    void setUp() throws IOException {
        assumeTrue(Files.exists(ENV_PATH), ".env 파일이 없으면 실제 S3 테스트를 건너뜀");
        properties = new Properties();
        try (FileInputStream input = new FileInputStream(ENV_PATH.toFile())) {
            properties.load(input);
        }
        assumeTrue(properties.getProperty("AWS_S3_ACCESS_KEY") != null, "AWS 키가 없으면 건너뜀");

        bucket = properties.getProperty("AWS_S3_BUCKET");
        key = "test/" + UUID.randomUUID();
        s3Client = S3Client.builder()
            .region(Region.of(properties.getProperty("AWS_S3_REGION")))
            .credentialsProvider(credentialsProvider())
            .build();
    }

    @AfterEach
    void tearDown() {
        if (s3Client != null) {
            s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
            s3Client.close();
        }
    }

    @Test
    void 업로드() {
        upload("upload-test");

        assertThat(s3Client.headObject(builder -> builder.bucket(bucket).key(key)).contentLength())
            .isEqualTo("upload-test".getBytes(StandardCharsets.UTF_8).length);
    }

    @Test
    void 다운로드() {
        upload("download-test");

        byte[] downloaded = s3Client.getObjectAsBytes(
            GetObjectRequest.builder().bucket(bucket).key(key).build()).asByteArray();

        assertThat(new String(downloaded, StandardCharsets.UTF_8)).isEqualTo("download-test");
    }

    @Test
    void PresignedUrl_생성() {
        upload("presigned-test");

        try (S3Presigner presigner = S3Presigner.builder()
            .region(Region.of(properties.getProperty("AWS_S3_REGION")))
            .credentialsProvider(credentialsProvider())
            .build()) {
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(GetObjectRequest.builder().bucket(bucket).key(key).build())
                .build();

            String url = presigner.presignGetObject(presignRequest).url().toString();

            assertThat(url).contains(bucket).contains(key).contains("X-Amz-Signature");
        }
    }

    private void upload(String content) {
        s3Client.putObject(
            PutObjectRequest.builder().bucket(bucket).key(key).build(),
            RequestBody.fromString(content));
    }

    private StaticCredentialsProvider credentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(
            properties.getProperty("AWS_S3_ACCESS_KEY"),
            properties.getProperty("AWS_S3_SECRET_KEY")));
    }
}
