package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channeldto.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channeldto.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserService userService;

    public BasicChannelService(ChannelRepository channelRepository, UserService userService) {
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    @Override
    public ChannelResponseDto createChannel(ChannelCreateRequestDto requestDto) {
        if (requestDto.getMemberIds() != null) {
            for (UUID memberId : requestDto.getMemberIds()) {
                if (Objects.isNull(userService.readUser(memberId))) {
                    throw new RuntimeException("존재하지 않는 유저: " + memberId);
                }
            }
        }
        List<UUID> memberIdList = requestDto.getMemberIds() == null ? new ArrayList<>() : new ArrayList<>(requestDto.getMemberIds());
        Channel channel = new Channel(requestDto.getChannelName(), memberIdList);
        channelRepository.save(channel);
        return ChannelResponseDto.from(channel);
    }

    @Override
    public ChannelResponseDto readChannel(UUID id) {
        Channel channel = channelRepository.findById(id);
        return ChannelResponseDto.from(channel);
    }

    @Override
    public List<ChannelResponseDto> readAllChannel() {
        List<Channel> channels = channelRepository.findAll();
        List<ChannelResponseDto> responses = new ArrayList<>();
        for (Channel channel : channels) {
            responses.add(ChannelResponseDto.from(channel));
        }
        return responses;
    }

    @Override
    public ChannelResponseDto updateChannel(UUID id, ChannelUpdateRequestDto requestDto) {
        Channel target = channelRepository.findById(id);
        if (Objects.isNull(target)) {
            throw new RuntimeException("해당 채널이 존재하지 않습니다.");
        }
        if (Objects.isNull(requestDto.getChannelName()) || requestDto.getChannelName().isBlank()) {
            throw new RuntimeException("유효하지 않은 채널명 입니다.");
        }
        target.setChannelName(requestDto.getChannelName());

        if (requestDto.getMemberIds() != null && !requestDto.getMemberIds().isEmpty()) {
            for (UUID memberId : requestDto.getMemberIds()) {
                if (Objects.isNull(userService.readUser(memberId))) {
                    throw new RuntimeException("존재하지 않는 유저입니다: " + memberId);
                }
            }
            target.setMemberIds(new ArrayList<>(requestDto.getMemberIds()));
        }
        target.setUpdatedAt();
        channelRepository.save(target);
        return ChannelResponseDto.from(target);
    }

    @Override
    public void deleteChannel(UUID id) {
        channelRepository.delete(id);
    }
}
