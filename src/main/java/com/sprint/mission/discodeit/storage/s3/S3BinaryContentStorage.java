package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentStorageException;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Slf4j
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@Component
public class S3BinaryContentStorage implements BinaryContentStorage {

  private static final long DEFAULT_PRESIGNED_URL_EXPIRATION_SECONDS = 600;

  private final String accessKey;
  private final String secretKey;
  private final String region;
  private final String bucket;
  private final long presignedUrlExpirationSeconds;

  private final S3Client s3Client;
  private final S3Presigner s3Presigner;

  public S3BinaryContentStorage(String accessKey, String secretKey, String region, String bucket) {
    this(accessKey, secretKey, region, bucket, DEFAULT_PRESIGNED_URL_EXPIRATION_SECONDS);
  }

  @Autowired
  public S3BinaryContentStorage(
      @Value("${discodeit.storage.s3.access-key}") String accessKey,
      @Value("${discodeit.storage.s3.secret-key}") String secretKey,
      @Value("${discodeit.storage.s3.region}") String region,
      @Value("${discodeit.storage.s3.bucket}") String bucket,
      @Value("${discodeit.storage.s3.presigned-url-expiration:600}") long presignedUrlExpirationSeconds
  ) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.region = region;
    this.bucket = bucket;
    this.presignedUrlExpirationSeconds = presignedUrlExpirationSeconds;
    this.s3Client = getS3Client();
    this.s3Presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(credentialsProvider())
        .build();
    log.info("S3 파일 저장소 초기화 완료: bucket={}, region={}", bucket, region);
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucket)
        .key(binaryContentId.toString())
        .build();
    try {
      s3Client.putObject(request, RequestBody.fromBytes(bytes));
      log.debug("S3 업로드 완료: id={}, size={}", binaryContentId, bytes.length);
    } catch (SdkException e) {
      throw new BinaryContentStorageException(binaryContentId, e);
    }
    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    GetObjectRequest request = GetObjectRequest.builder()
        .bucket(bucket)
        .key(binaryContentId.toString())
        .build();
    try {
      return s3Client.getObject(request);
    } catch (NoSuchKeyException e) {
      throw new BinaryContentNotFoundException(binaryContentId);
    } catch (SdkException e) {
      throw new BinaryContentStorageException(binaryContentId, e);
    }
  }

  @Override
  public ResponseEntity<Void> download(BinaryContentDto metaData) {
    String presignedUrl = generatePresignedUrl(metaData.id().toString(), metaData.contentType());
    log.info("파일 다운로드(Presigned URL 리다이렉트): id={}, fileName={}",
        metaData.id(), metaData.fileName());

    return ResponseEntity
        .status(HttpStatus.FOUND)
        .location(URI.create(presignedUrl))
        .build();
  }

  private S3Client getS3Client() {
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(credentialsProvider())
        .build();
  }

  private String generatePresignedUrl(String key, String contentType) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucket)
        .key(key)
        .responseContentType(contentType)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofSeconds(presignedUrlExpirationSeconds))
        .getObjectRequest(getObjectRequest)
        .build();

    return s3Presigner.presignGetObject(presignRequest).url().toString();
  }

  private StaticCredentialsProvider credentialsProvider() {
    return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
  }
}
