package com.sprint.mission.discodeit.channel.domain.repository;

import com.sprint.mission.discodeit.channel.domain.entity.Channel;
import com.sprint.mission.discodeit.channel.domain.entity.ChannelType;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {
    List<Channel> findAllByIdIn(List<UUID> idList);
    List<Channel> findAllByChannelType(ChannelType channelType);

    default Channel getByIdOrThrow(UUID channelId) {
        return findById(channelId).orElseThrow(() -> new CustomException(CustomErrorCode.CHANNEL_NOT_FOUND));
    }

    @Query("""
    select distinct channel
    from Channel channel
    where channel.channelType = :channelType
       or exists (
           select 1
           from ReadStatus readStatus
           where readStatus.channel = channel
             and readStatus.user.id = :userId
       )
    """)
    List<Channel> findAccessibleChannelsByUserId(
        UUID userId,
        ChannelType channelType
    );
}
