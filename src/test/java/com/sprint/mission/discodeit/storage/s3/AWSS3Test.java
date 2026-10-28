package com.sprint.mission.discodeit.storage.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

/**
 * 실제 S3 버킷에 접근하는 학습용 테스트. 프로젝트 루트의 .env에 AWS 정보가 없으면 건너뛴다.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AWSS3Test {

  private static final byte[] CONTENT = "hello discodeit".getBytes(StandardCharsets.UTF_8);

  private final String key = "aws-s3-test/" + UUID.randomUUID();

  private String bucket;
  private S3Client s3Client;
  private S3Presigner s3Presigner;

  @BeforeAll
  void setUp() throws IOException {
    Properties env = loadEnv();
    assumeTrue(!env.getProperty("AWS_S3_ACCESS_KEY", "").isBlank(), ".env에 AWS 정보가 없어 건너뜀");

    bucket = env.getProperty("AWS_S3_BUCKET");
    Region region = Region.of(env.getProperty("AWS_S3_REGION"));
    StaticCredentialsProvider credentials = StaticCredentialsProvider.create(
        AwsBasicCredentials.create(
            env.getProperty("AWS_S3_ACCESS_KEY"),
            env.getProperty("AWS_S3_SECRET_KEY")));

    s3Client = S3Client.builder().region(region).credentialsProvider(credentials).build();
    s3Presigner = S3Presigner.builder().region(region).credentialsProvider(credentials).build();
  }

  @AfterAll
  void tearDown() {
    if (s3Client != null) {
      s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
      s3Client.close();
      s3Presigner.close();
    }
  }

  @Test
  @Order(1)
  @DisplayName("업로드")
  void upload() {
    s3Client.putObject(
        PutObjectRequest.builder().bucket(bucket).key(key).contentType("text/plain").build(),
        RequestBody.fromBytes(CONTENT));
  }

  @Test
  @Order(2)
  @DisplayName("다운로드")
  void download() {
    ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(
        GetObjectRequest.builder().bucket(bucket).key(key).build());

    assertThat(response.asByteArray()).isEqualTo(CONTENT);
  }

  @Test
  @Order(3)
  @DisplayName("PresignedUrl 생성")
  void presignedUrl() throws Exception {
    String url = s3Presigner.presignGetObject(GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .getObjectRequest(GetObjectRequest.builder().bucket(bucket).key(key).build())
            .build())
        .url().toString();

    HttpResponse<byte[]> response = HttpClient.newHttpClient().send(
        HttpRequest.newBuilder(URI.create(url)).GET().build(),
        HttpResponse.BodyHandlers.ofByteArray());

    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(response.body()).isEqualTo(CONTENT);
  }

  static Properties loadEnv() throws IOException {
    Properties env = new Properties();
    Path path = Path.of(".env");
    if (Files.exists(path)) {
      try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
        env.load(reader);
      }
    }
    return env;
  }
}
