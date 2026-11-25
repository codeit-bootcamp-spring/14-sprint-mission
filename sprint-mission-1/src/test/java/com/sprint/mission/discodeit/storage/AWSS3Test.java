package com.sprint.mission.discodeit.storage;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@EnabledIf("envFileExists")
class AWSS3Test {

    private static final String PREFIX = "test/";
    private static final String CONTENT = "hello s3";
    private static final List<String> createdKeys = new ArrayList<>();

    private static S3Client s3Client;
    private static S3Presigner presigner;
    private static String bucket;

    private String fixtureKey;

    static boolean envFileExists() {
        return Files.exists(Path.of(".env"));
    }

    @BeforeAll
    static void setUp() throws IOException {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Path.of(".env"))) {
            props.load(in);
        }
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
            props.getProperty("AWS_S3_ACCESS_KEY"),
            props.getProperty("AWS_S3_SECRET_KEY"));
        Region region = Region.of(props.getProperty("AWS_S3_REGION"));
        bucket = props.getProperty("AWS_S3_BUCKET");

        s3Client = S3Client.builder()
            .region(region)
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build();
        presigner = S3Presigner.builder()
            .region(region)
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build();
    }

    // 테스트마다 독립된 객체를 준비해 다른 테스트에 의존하지 않게 한다
    @BeforeEach
    void putFixture() {
        fixtureKey = putObject(CONTENT);
    }

    // 테스트가 만든 객체만 지워서 실행할 때마다 버킷에 쌓이지 않게 한다
    @AfterAll
    static void cleanUp() {
        if (s3Client == null) {
            return;
        }
        if (!createdKeys.isEmpty()) {
            List<ObjectIdentifier> ids = createdKeys.stream()
                .map(key -> ObjectIdentifier.builder().key(key).build())
                .toList();
            var response = s3Client.deleteObjects(
                b -> b.bucket(bucket).delete(Delete.builder().objects(ids).build()));
            // deleteObjects는 개별 삭제 실패를 예외 대신 응답에 담으므로 직접 확인한다
            assertThat(response.errors()).as("테스트 객체 삭제 실패").isEmpty();
        }
        s3Client.close();
        presigner.close();
    }

    private static String putObject(String content) {
        String key = PREFIX + UUID.randomUUID();
        createdKeys.add(key);
        s3Client.putObject(
            PutObjectRequest.builder().bucket(bucket).key(key).contentType("text/plain").build(),
            RequestBody.fromString(content));
        return key;
    }

    @Test
    @DisplayName("업로드한 객체가 버킷에 저장된다")
    void upload() {
        String key = putObject("upload");

        long length = s3Client.headObject(b -> b.bucket(bucket).key(key)).contentLength();

        assertThat(length).isEqualTo("upload".length());
    }

    @Test
    @DisplayName("저장된 객체를 내려받으면 업로드한 내용과 같다")
    void download() {
        ResponseBytes<GetObjectResponse> bytes = s3Client.getObjectAsBytes(
            GetObjectRequest.builder().bucket(bucket).key(fixtureKey).build());

        assertThat(bytes.asUtf8String()).isEqualTo(CONTENT);
    }

    @Test
    @DisplayName("Presigned URL로 GET 요청하면 200과 업로드한 내용을 반환한다")
    void presignedUrl() throws Exception {
        PresignedGetObjectRequest presigned = presigner.presignGetObject(
            GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(GetObjectRequest.builder().bucket(bucket).key(fixtureKey).build())
                .build());

        HttpResponse<String> response = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create(presigned.url().toString())).build(),
            HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo(CONTENT);
    }
}
