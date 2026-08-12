package com.sprint.mission.discodeit.service.application.readstatus;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.service.domain.channel.ChannelDomainService;
import com.sprint.mission.discodeit.service.domain.readstatus.ReadStatusDomainService;
import com.sprint.mission.discodeit.service.domain.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadStatusApplicationServiceImpl implements ReadStatusApplicationService {

    private final ReadStatusDomainService readStatusDomainService;
    private final UserDomainService userDomainService;
    private final ChannelDomainService channelDomainService;


    @Override
    public ReadStatusResponseDto create(ReadStatusCreateRequestDto readStatusCreateRequest) {
        // 존재하는 유저와 채널인지 확인
        userDomainService.findById(readStatusCreateRequest.getUserId());
        channelDomainService.findById(readStatusCreateRequest.getChannelId());

        ReadStatus readStatus = ReadStatus.create(
                readStatusCreateRequest.getUserId(),
                readStatusCreateRequest.getChannelId()
        );
        ReadStatus createdReadStatus = readStatusDomainService.create(readStatus);

        log.info(
                "ReadStatus 생성 완료: readStatusId={}, userId={}, channelId={}",
                createdReadStatus.getId(),
                createdReadStatus.getUserId(),
                createdReadStatus.getChannelId()
        );

        return ReadStatusResponseDto.from(createdReadStatus);
    }


    @Override
    public ReadStatusResponseDto findById(UUID readStatusId) {
        log.debug(
                "ReadStatus 단건 조회: readStatusId={}",
                readStatusId
        );
        ReadStatus readStatus = readStatusDomainService.findById(readStatusId);
        return ReadStatusResponseDto.from(readStatus);
    }


    @Override
    public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
        userDomainService.findById(userId);

        List<ReadStatusResponseDto> responses =
                readStatusDomainService.findAllByUserId(userId)
                        .stream()
                        .map(ReadStatusResponseDto::from)
                        .toList();

        log.debug("User ReadStatus 목록 조회 완료: userId={}, count={}", userId, responses.size());

        return responses;
    }


    @Override
    public ReadStatusResponseDto update(UUID readStatusId) {
        ReadStatus updatingReadStatus = readStatusDomainService.findById(readStatusId);
        updatingReadStatus.markAsRead();
        ReadStatus updatedReadStatus = readStatusDomainService.update(updatingReadStatus);

        log.debug(
                "ReadStatus 읽음 시간 갱신: readStatusId={}, userId={}, channelId={}, lastReadAt={}",
                updatedReadStatus.getId(),
                updatedReadStatus.getUserId(),
                updatedReadStatus.getChannelId(),
                updatedReadStatus.getLastReadAt()
        );

        return ReadStatusResponseDto.from(updatedReadStatus);
    }


    @Override
    public void delete(UUID readStatusId) {
        log.info("ReadStatus 삭제 시작: readStatusId={}", readStatusId);

        readStatusDomainService.delete(readStatusId);

        log.info("ReadStatus 삭제 완료: readStatusId={}", readStatusId);
    }
}
