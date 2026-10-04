package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.IService.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BasicChannelService  implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public ChannelResponseDto createPublic(PublicChannelCreateRequestDto request) {
        log.debug("public 채널 생성 요청: name={}", request.name());
        Channel channel = request.toEntity();
        channelRepository.save(channel);
        log.info("public 채널 생성 완료: channelId={}", channel.getId());
        return ChannelResponseDto.from(channel, List.of(), null);
    }

    @Override
    public ChannelResponseDto createPrivate(PrivateChannelCreateRequestDto request) {
        log.debug("private 채널 생성 요청: participantCount={}", request.participantIds().size());

        Channel channel = new Channel(ChannelType.PRIVATE);
        channelRepository.save(channel);

        for (UUID userId : request.participantIds()) {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 유저를 참여자로 채널 생성 시도: userId={}", userId);
                    return new IllegalArgumentException("존재하지 않는 유저입니다: " + userId);
                });

            ReadStatus readStatus = new ReadStatus(user, channel, Instant.now());
            readStatusRepository.save(readStatus);
        }
        log.info("private 채널 생성 완료: channelId={}, participantCount={}",
            channel.getId(), request.participantIds().size());
        return ChannelResponseDto.from(channel, request.participantIds(), null);
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelResponseDto find(UUID id) {
        Channel channel = channelRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 채널입니다."));

        Instant lastMessageAt = messageRepository.findTopByChannel_IdOrderByCreatedAtDesc(channel.getId())
            .map(Message::getCreatedAt)
            .orElse(null);

        List<UUID> participantIds = channel.getType() == ChannelType.PRIVATE
            ? readStatusRepository.findAllByChannel_Id(channel.getId()).stream()
            .map(rs -> rs.getUser().getId())
            .toList()
            : List.of();

        return ChannelResponseDto.from(channel, participantIds, lastMessageAt);
    }

    @Override
    public ChannelResponseDto update(UUID id, ChannelUpdateRequestDto updateRequest) {
        log.debug("채널 수정 요청: channelId={}", id);

        Channel channel = channelRepository.findById(id)
            .orElseThrow(() ->{
                log.warn("존재하지 않는 채널 수정 시도: channelId={}", id);
                return new NoSuchElementException("존재하지 않는 채널입니다.");
            });

        if (channel.getType() == ChannelType.PRIVATE) {
            log.warn("PRIVATE 채널 수정 시도: channelId={}", id);
            throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.update(updateRequest.newName(), updateRequest.newDescription());


        Instant lastMessageAt = messageRepository.findTopByChannel_IdOrderByCreatedAtDesc(channel.getId())
            .map(Message::getCreatedAt)
            .orElse(null);

        log.info("채널 수정 완료: channelId={}", id);
        return ChannelResponseDto.from(channel, List.of(), lastMessageAt);
    }

    @Override
    public void delete(UUID id) {
        log.debug("채널 삭제 요청: channelId={}", id);

        Channel channel = channelRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("존재하지 않는 채널 삭제 시도: channelId={}", id);
                return new NoSuchElementException("존재하지 않는 채널입니다.");
            });

        channelRepository.deleteById(id);
        log.info("채널 삭제 완료: channelId={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        List<UUID> privateChannelIds = readStatusRepository.findAllByUser_Id(userId)
            .stream()
            .map(rs -> rs.getChannel().getId())
            .toList();

        List<Channel> channels = channelRepository.findAll().stream()
            .filter(channel ->
                channel.getType() == ChannelType.PUBLIC
                    || privateChannelIds.contains(channel.getId())
            )
            .toList();

        return channels.stream()
            .map(channel -> {
                Instant lastMessageAt = messageRepository.findTopByChannel_IdOrderByCreatedAtDesc(channel.getId())
                    .map(Message::getCreatedAt)
                    .orElse(null);

                List<UUID> participantIds = channel.getType() == ChannelType.PRIVATE
                    ? readStatusRepository.findAllByChannel_Id(channel.getId()).stream()
                    .map(rs -> rs.getUser().getId())
                    .toList()
                    : List.of();

                return ChannelResponseDto.from(channel, participantIds, lastMessageAt);
            })
            .toList();
    }
}
