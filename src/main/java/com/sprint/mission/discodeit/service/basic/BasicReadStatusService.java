package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(@Valid ReadStatusCreateDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow();
        Channel channel = channelRepository.findById(dto.getChannelId())
                .orElseThrow();

        if (readStatusRepository.existsByUserAndChannel(user.getId(), channel.getId())) {
            throw new IllegalArgumentException("ReadStatus 이미 있어서 못만듦 ㅋ");
        }

        return dto.toReadStatus();
    }

    @Override
    public ReadStatus getReadStatus(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow();
    }

    @Override
    public List<ReadStatus> getAllReadStatusByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public void updateReadStatus(UUID id) {
        // 요구사항 : DTO를 활용해 파라미터를 그룹화합니다 (수정 대상 객체의 id 파라미터, 수정할 값 파라미터)
        // 아니 근데 readStatus애서 수정할 필드라곤 마지막 메시지 읽은 시간밖에 없어서 파라미터도 필요 없는데
        // 왜 DTO를 만들라고 했을까?
        readStatusRepository.update(id);
    }

    @Override
    public void deleteReadStatus(UUID id) {
        readStatusRepository.deleteById(id);
    }




}
