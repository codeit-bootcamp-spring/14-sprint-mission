package com.sprint.mission.discodeit.readstatus.domain.service;

import com.sprint.mission.discodeit.readstatus.domain.entity.ReadStatus;
import com.sprint.mission.discodeit.readstatus.domain.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ReadStatusServiceImpl implements ReadStatusService{
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatus createReadStatus(ReadStatus readStatus) {
        // 동일 필드 방지 검증
        if(readStatusRepository.existsByUserAndChannel(
            readStatus.getUser(), readStatus.getChannel()
        )){
            throw new CustomException(CustomErrorCode.READ_STATUS_DUPLICATE);
        }

        return readStatusRepository.save(readStatus);
    }
}
