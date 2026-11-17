package com.sprint.mission.discodeit.storage.s3;

import static org.assertj.core.api.Assertions.*;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

// .env가 없는 환경(CI, 다른 팀원 PC)에서는 실행하지 않음
@EnabledIf("envFileExists")
class AWSS3Test {

    private static final Path ENV_PATH = Path.of(".env");
    private static Properties props;

    private S3Client s3Client;
    private S3Presigner presigner;
    private String bucket;
    private String key;

    static boolean envFileExists() {
        return Files.exists(ENV_PATH);
    }

    @BeforeAll
    static void loadEnv() throws Exception {
        props = new Properties();
        try (InputStream in = Files.newInputStream(ENV_PATH)) {
            props.load(in);   // KEY=VALUE 형식이라 .env를 그대로 읽을 수 있음
        }
    }

    @BeforeEach
    void setUp() {
        StaticCredentialsProvider credentials = StaticCredentialsProvider.create(
            AwsBasicCredentials.create(
                props.getProperty("AWS_S3_ACCESS_KEY"),
                props.getProperty("AWS_S3_SECRET_KEY")));
        Region region = Region.of(props.getProperty("AWS_S3_REGION"));

        s3Client = S3Client.builder().region(region).credentialsProvider(credentials).build();
        presigner = S3Presigner.builder().region(region).credentialsProvider(credentials).build();
        bucket = props.getProperty("AWS_S3_BUCKET");
        key = "test/" + UUID.randomUUID();   // 테스트마다 다른 파일 이름
    }

    @AfterEach
    void tearDown() {
        // 테스트로 올린 파일 정리 (없어도 에러 안 남)
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
        s3Client.close();
        presigner.close();
    }

    private void upload(String content) {
        s3Client.putObject(
            PutObjectRequest.builder().bucket(bucket).key(key).contentType("text/plain").build(),
            RequestBody.fromString(content));
    }

    @Test
    @DisplayName("업로드 - 버킷에 파일이 올라간다")
    void upload_success() {
        upload("hello s3");

        HeadObjectResponse head = s3Client.headObject(
            HeadObjectRequest.builder().bucket(bucket).key(key).build());
        assertThat(head.contentLength()).isEqualTo("hello s3".length());
    }

    @Test
    @DisplayName("다운로드 - 올린 파일을 그대로 받는다")
    void download_success() {
        upload("hello s3");

        String downloaded = s3Client.getObjectAsBytes(
            GetObjectRequest.builder().bucket(bucket).key(key).build()).asUtf8String();

        assertThat(downloaded).isEqualTo("hello s3");
    }

    @Test
    @DisplayName("Presigned URL - 임시 링크로 파일을 받을 수 있다")
    void presignedUrl_success() throws Exception {
        upload("hello s3");

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .getObjectRequest(r -> r.bucket(bucket).key(key))
            .build();
        String url = presigner.presignGetObject(presignRequest).url().toString();

        assertThat(url).contains(bucket).contains("X-Amz-Signature");

        // 링크로 실제 다운로드가 되는지 확인
        HttpResponse<String> response = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create(url)).GET().build(),
            HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).isEqualTo("hello s3");
    }
}