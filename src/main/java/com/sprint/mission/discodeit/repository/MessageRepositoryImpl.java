package com.sprint.mission.discodeit.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.domain.message.QMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QMessage message = QMessage.message;

    @Override
    public Optional<Instant> findLatestMessageByChannelId(UUID channelId) {
        return queryFactory
                .select(message.createdAt.max())
                .from(message)
                .stream().findAny();
    }
}
