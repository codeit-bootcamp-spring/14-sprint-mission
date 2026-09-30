package com.sprint.mission.discodeit.message.domain.repository;

import com.sprint.mission.discodeit.channel.domain.entity.Channel;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.message.domain.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    default Message getByIdOrThrow(UUID channelId) {
        return findById(channelId).orElseThrow(() -> new CustomException(CustomErrorCode.MESSAGE_NOT_FOUND));
    }

    // findLastMessage
    // 1. 페치 조인으로 마지막 메시지 + 채널을 조인한다 // 2. 사용자 정의(프로젝션) dto 를 통해 필요 필드만을 추출한다.
    @Query("""
      select new com.sprint.mission.discodeit.message.domain.repository.MessageRepository$ChannelLastMessage(
              m.channel.id,
              max(m.createdAt)
          )
          from Message m
          where m.channel in :channelList
          group by m.channel.id
    """)
    List<ChannelLastMessage> findLastMessageByChannels(List<Channel> channelList);
    record ChannelLastMessage(
        UUID channelId,                     // ㅇㅁ
        Instant lastMessageAt
    ) { }

    // 슬라이스로 쪼갬
    Slice<Message> findAllByChannelId(UUID channelId, Pageable pageable);
}
