package com.sprint.mission.discodeit.storage.s3;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Properties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AWSS3Test {

    private static final String TEST_KEY = "test-file.txt";
    private static final String TEST_CONTENT = "Hello AWS S3 Discodeit!";
    private static String accessKey;
    private static String secretKey;
    private static String region;
    private static String bucket;
    private static S3Client s3Client;

    @BeforeAll
    static void setUp() throws IOException {
        // Properties 클래스를 활용하여 .env 파일 로드
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(".env")) {
            properties.load(fis);
        }

        accessKey = properties.getProperty("AWS_S3_ACCESS_KEY");
        secretKey = properties.getProperty("AWS_S3_SECRET_KEY");
        region = properties.getProperty("AWS_S3_REGION");
        bucket = properties.getProperty("AWS_S3_BUCKET");

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        s3Client = S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build();
    }

    @Test
    @Order(1)
    @DisplayName("S3 업로드 테스트")
    void uploadTest() {
        byte[] bytes = TEST_CONTENT.getBytes(StandardCharsets.UTF_8);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucket)
            .key(TEST_KEY)
            .contentType("text/plain")
            .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
        // 에러 없이 실행되면 업로드 성공
    }

    @Test
    @Order(2)
    @DisplayName("S3 다운로드 테스트")
    void downloadTest() throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(TEST_KEY)
            .build();

        try (InputStream inputStream = s3Client.getObject(getObjectRequest)) {
            String downloadedContent = new String(inputStream.readAllBytes(),
                StandardCharsets.UTF_8);
            assertThat(downloadedContent).isEqualTo(TEST_CONTENT);
        }
    }

    @Test
    @Order(3)
    @DisplayName("S3 PresignedUrl 생성 테스트")
    void generatePresignedUrlTest() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        try (S3Presigner presigner = S3Presigner.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()) {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(TEST_KEY)
                .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10)) // 10분 유효
                .getObjectRequest(getObjectRequest)
                .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            String url = presignedRequest.url().toString();

            System.out.println("생성된 Presigned URL: " + url);
            assertThat(url).contains(bucket).contains(TEST_KEY);
        }
    }
}