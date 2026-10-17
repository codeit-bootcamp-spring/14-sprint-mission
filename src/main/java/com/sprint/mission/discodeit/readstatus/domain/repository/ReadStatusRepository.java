package com.sprint.mission.discodeit.readstatus.domain.repository;

import com.sprint.mission.discodeit.channel.domain.entity.Channel;
import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.message.domain.entity.Message;
import com.sprint.mission.discodeit.readstatus.domain.entity.ReadStatus;
import com.sprint.mission.discodeit.user.domain.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {
    List<ReadStatus> findAllByChannelId(UUID channelId);
    List<ReadStatus> findAllByUserId(UUID userId);

    boolean existsByUserAndChannel(User user, Channel channel);

    // 채널 리스트를 받고 유저 페치조인 + 유저와 연결된 상태 페치조인 + 바이너리컨텐트 페치조인
    @Query("""
      select r
          from ReadStatus r
          join fetch r.channel
          join fetch r.user
          join fetch r.user.userStatus
          left join fetch r.user.profileImage
          where r.channel in :channelList
    """)
    List<ReadStatus> findAllParticipantDetailByChannels(List<Channel> channelList);

    // N+1 방지
    @Query("""
        select readStatus.user.id
        from ReadStatus readStatus
        where readStatus.channel.id = :channelId
        """)
    List<UUID> findUserIdsByChannelId(UUID channelId);

    default ReadStatus getByIdOrThrow(UUID readStatusId) {
        return findById(readStatusId).orElseThrow(() -> new CustomException(CustomErrorCode.READ_STATUS_NOT_FOUND));
    }
}
