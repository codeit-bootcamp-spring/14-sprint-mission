package com.sprint.mission.discodeit.adaptor.persistence;

import static com.sprint.mission.discodeit.domain.QChannel.channel;
import static com.sprint.mission.discodeit.domain.QReadStatus.readStatus;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelType;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChannelQRepositoryImpl implements ChannelQRepository {

  private final JPAQueryFactory jpaQueryFactory;


  @Override
  public List<Channel> findAllByUserId(UUID userId) {

    return jpaQueryFactory
        .selectFrom(channel)
        .leftJoin(readStatus)
        .on(
            readStatus.channel.eq(channel)
                .and(readStatus.user.id.eq(userId))
        )
        .where(
            channel.type.eq(ChannelType.PUBLIC)
                .or(readStatus.id.isNotNull())
        ).fetch();
  }
}
