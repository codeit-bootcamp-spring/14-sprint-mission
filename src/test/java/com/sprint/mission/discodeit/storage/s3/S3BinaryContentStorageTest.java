package com.sprint.mission.discodeit.storage.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class S3BinaryContentStorageTest {

  @Test
  @DisplayName("download는 Presigned URL로 302 리다이렉트한다 (서명은 로컬 계산이라 실제 키 불필요)")
  void download_redirectsToPresignedUrl() {
    S3BinaryContentStorage storage = new S3BinaryContentStorage(
        "test-access-key", "test-secret-key", "ap-northeast-2", "test-bucket");
    UUID id = UUID.randomUUID();
    BinaryContentDto metaData = new BinaryContentDto(id, "profile.png", 10L, "image/png");

    ResponseEntity<Void> response = storage.download(metaData);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    assertThat(response.getHeaders().getLocation()).isNotNull();
    String location = response.getHeaders().getLocation().toString();
    assertThat(location)
        .contains("test-bucket")
        .contains(id.toString())
        .contains("X-Amz-Signature")
        .contains("X-Amz-Expires=600")
        .contains("response-content-type=image%2Fpng");
  }

  @Test
  @DisplayName("put으로 올린 파일을 get으로 그대로 읽는다 (.env에 AWS 정보가 있을 때만)")
  void putAndGet() throws Exception {
    Properties env = AWSS3Test.loadEnv();
    assumeTrue(!env.getProperty("AWS_S3_ACCESS_KEY", "").isBlank(), ".env에 AWS 정보가 없어 건너뜀");
    S3BinaryContentStorage storage = new S3BinaryContentStorage(
        env.getProperty("AWS_S3_ACCESS_KEY"),
        env.getProperty("AWS_S3_SECRET_KEY"),
        env.getProperty("AWS_S3_REGION"),
        env.getProperty("AWS_S3_BUCKET"));
    UUID id = UUID.randomUUID();
    byte[] bytes = "s3 storage test".getBytes(StandardCharsets.UTF_8);

    UUID savedId = storage.put(id, bytes);

    assertThat(savedId).isEqualTo(id);
    try (InputStream inputStream = storage.get(id)) {
      assertThat(inputStream.readAllBytes()).isEqualTo(bytes);
    }
  }
}
