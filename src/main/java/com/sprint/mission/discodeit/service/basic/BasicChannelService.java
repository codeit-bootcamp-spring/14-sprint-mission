package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.NoSuchElementException;
import com.sprint.mission.discodeit.exception.PrivateChannelUpdateNotAllowedException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public ChannelResponseDto create(ChannelCreateRequestDto request) {
        Channel channel = request.toEntity();
        if (channel.getChannelType() == ChannelType.PRIVATE) {

            return privateChannelCreate(channel, request.userIds());
        } else {
            return publicChannelCreate(channel);
        }
    }

    @Override
    public ChannelResponseDto find(UUID id) {
        Channel channel = channelCheck(id);

        List<UUID> userIds = new ArrayList<>();
        if (channel.getChannelType() == ChannelType.PRIVATE) {
            userIds = readStatusRepository.findAllByChannelId(channel.getId());
        }

        return ChannelResponseDto.from(channel.getId(), channel.getChannelType(), channel.getTitle(),
                channel.getMemo(), userIds);
    }

    // 특정 유저가 볼 수 있는 채널 조회
    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        return channelRepository.findAll().stream()
                .filter(channel -> channel.getChannelType() == ChannelType.PUBLIC ||
                        readStatusRepository.findAllByChannelId(channel.getId()).contains(userId))
                .map(channel -> find(channel.getId()))
                .toList();
    }


    @Override
    public void update(ChannelUpdateRequestDto request) {
        Channel channel1 = channelCheck(request.id());
        if(channel1.getChannelType() == ChannelType.PRIVATE){
            throw new PrivateChannelUpdateNotAllowedException();
        }
        channel1.update(request.channelType(), request.title(), request.memo());
        channelRepository.save(channel1);
    }

    @Override
    public void delete(UUID id) {

        messageRepository.deleteByChannelId(id);
        readStatusRepository.deleteByChannelId(id);
        channelRepository.deleteById(id);
    }


    private Channel channelCheck(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    private ChannelResponseDto publicChannelCreate(Channel channel) {

        channelRepository.save(channel);
        return ChannelResponseDto.from(channel.getId(), channel.getChannelType(), channel.getTitle(), channel.getMemo(), List.of());
    }


    private ChannelResponseDto privateChannelCreate(Channel channel, List<UUID> userIds) {
        List<ReadStatus> readStatuses = new ArrayList<>();
        for (UUID userId : userIds) {
            ReadStatus readStatus = new ReadStatus(userId, channel.getId());

            readStatuses.add(readStatus);
            // 여기 read statusRepository 에다가 넣기
            readStatusRepository.save(readStatus);
        }
        List<UUID> list = readStatuses.stream().map(ReadStatus::getUserId).toList();
        channelRepository.save(channel);
        return ChannelResponseDto.from(channel.getId(), channel.getChannelType(), channel.getTitle(), channel.getMemo(), list);
    }


}
