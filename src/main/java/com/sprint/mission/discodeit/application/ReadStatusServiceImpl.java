package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.adaptor.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.adaptor.dto.readStatus.ReadStatusResponse;
import com.sprint.mission.discodeit.adaptor.dto.readStatus.ReadStausRequest;
import com.sprint.mission.discodeit.application.channel.ChannelRepository;
import com.sprint.mission.discodeit.application.user.required.UserRepository;
import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.ReadStatus;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReadStatusServiceImpl implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ReadStatusResponse create(ReadStatusCreateRequest readStatusCreateRequest) {
    userRepository.findById(readStatusCreateRequest.userId())
        .orElseThrow(() -> new DiscodeitRuntimeException(ExceptionType.USER_NOT_FOUND));
    channelRepository.findChannel(readStatusCreateRequest.channelId())
        .orElseThrow(() -> new DiscodeitRuntimeException(ExceptionType.CHANNEL_NOT_FOUND));
    ReadStatus readStatus = ReadStatus.create(readStatusCreateRequest.userId(),
        readStatusCreateRequest.channelId());
    ReadStatus savedReadStatus = readStatusRepository.save(readStatus);
    return ReadStatusResponse.from(savedReadStatus);
  }

  @Override
  public List<ReadStatusResponse> findByChannelId(UUID channelId) {
    return readStatusRepository.findByChannel(channelId).stream().map(ReadStatusResponse::from)
        .toList();
  }

  @Override
  public ReadStatusResponse findById(UUID uuid) {
    ReadStatus readStatus = readStatusRepository.find(uuid).orElseThrow(
        () -> new DiscodeitRuntimeException(ExceptionType.READSTATUS_NOT_FOUND));
    return ReadStatusResponse.from(readStatus);
  }

  @Override
  public List<ReadStatusResponse> findByUserId(UUID userId) {
    return readStatusRepository.findByUser(userId).stream().map(ReadStatusResponse::from)
        .toList();
  }

  @Override
  public void delete(UUID uuid) {
    readStatusRepository.delete(uuid);
  }

  @Override
  public ReadStatusResponse update(ReadStausRequest readStausRequest, UUID readStatusId) {
    ReadStatus readStatus = readStatusRepository.find(readStatusId)
        .orElseThrow(() -> new DiscodeitRuntimeException(
            ExceptionType.READSTATUS_NOT_FOUND));
    ReadStatus updatedReadStatus = readStatus.update(readStausRequest.recentReadAt());
    ReadStatus savedReadStatus = readStatusRepository.save(updatedReadStatus);
    return ReadStatusResponse.from(savedReadStatus);
  }
}
