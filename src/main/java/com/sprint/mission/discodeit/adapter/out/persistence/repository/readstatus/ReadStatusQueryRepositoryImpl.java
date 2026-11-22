package com.sprint.mission.discodeit.adapter.out.persistence.repository.readstatus;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.core.domain.readstatus.QReadStatus;
import com.sprint.mission.discodeit.core.domain.readstatus.ReadStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReadStatusQueryRepositoryImpl implements ReadStatusQueryRepository {

    private final JPAQueryFactory queryFactory;
    private static final QReadStatus readStatus = QReadStatus.readStatus;

    @Override
    public List<UUID> findAllUserIdByChannelId(UUID channelId) {
        return queryFactory
                .select(readStatus.user.id)
                .from(readStatus)
                .where(readStatus.channel.id.eq(channelId))
                .fetch();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return queryFactory
                .selectFrom(readStatus)
                .where(readStatus.user.id.eq(userId))
                .fetch();
    }
}
