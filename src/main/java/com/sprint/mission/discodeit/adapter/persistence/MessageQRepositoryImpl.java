package com.sprint.mission.discodeit.adapter.persistence;

import static com.sprint.mission.discodeit.domain.QChannel.channel;
import static com.sprint.mission.discodeit.domain.QMessage.message;
import static com.sprint.mission.discodeit.domain.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.application.message.required.MessageQRepository;
import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageQRepositoryImpl implements MessageQRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Message> findAllByCursor(UUID channelId, Instant cursor, UUID idAfter, int limit) {
    return jpaQueryFactory.selectFrom(message)
        .join(message.channel, channel)
        .fetchJoin()
        .leftJoin(message.author, user)
        .fetchJoin()
        .where(message.channel.id.eq(channelId).and(cursorCond(cursor, idAfter)))
        .orderBy(message.createdAt.desc(), message.id.desc())
        .limit(limit)
        .fetch();
  }

  private BooleanExpression cursorCond(Instant cursor, UUID idAfter) {
    if (cursor == null && idAfter == null) {
      return null;
    }
    if (cursor == null) {
      throw new DiscodeitRuntimeException(ExceptionType.INVALID_INFO);
    }
    return message.createdAt.lt(cursor)
        .or(message.createdAt.eq(cursor)
            .and(message.id.lt(idAfter)));
  }
}
