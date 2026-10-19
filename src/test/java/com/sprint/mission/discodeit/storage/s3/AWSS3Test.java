package com.sprint.mission.discodeit.storage.s3;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@EnabledIf("envFileExists")   // .env 없는 환경(CI)에서는 건너뜀
class AWSS3Test {

    private static S3Client s3Client;
    private static S3Presigner presigner;
    private static String bucket;
    private static final String KEY = "test/" + UUID.randomUUID() + ".txt";

    static boolean envFileExists() {
        return Files.exists(Path.of(".env"));
    }

    @BeforeAll
    static void setUp() throws IOException {
        // Properties 는 KEY=VALUE 형식을 그대로 읽는다 → .env 를 별도 라이브러리 없이 로드
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

    @Test
    void upload() {
        var response = s3Client.putObject(
            PutObjectRequest.builder().bucket(bucket).key(KEY).contentType("text/plain").build(),
            RequestBody.fromString("hello s3"));
        assertThat(response.sdkHttpResponse().isSuccessful()).isTrue();
        System.out.printf("업로드 완료: s3://%s/%s%n", bucket, KEY);
    }

    @Test
    void download() {
        upload();
        ResponseBytes<GetObjectResponse> bytes = s3Client.getObjectAsBytes(
            GetObjectRequest.builder().bucket(bucket).key(KEY).build());
        assertThat(bytes.asUtf8String()).isEqualTo("hello s3");
    }

    @Test
    void presignedUrl() {
        upload();
        PresignedGetObjectRequest presigned = presigner.presignGetObject(
            GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(GetObjectRequest.builder().bucket(bucket).key(KEY).build())
                .build());
        System.out.println(presigned.url());   // 이 주소를 브라우저에 붙여넣어 보기
        assertThat(presigned.url().toString()).contains(bucket);
    }
}
