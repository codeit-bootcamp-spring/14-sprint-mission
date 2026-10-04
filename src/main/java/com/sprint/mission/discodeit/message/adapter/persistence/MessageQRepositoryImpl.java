package com.sprint.mission.discodeit.message.adapter.persistence;


import static com.sprint.mission.discodeit.channel.domain.QChannel.channel;
import static com.sprint.mission.discodeit.message.domain.QMessage.message;
import static com.sprint.mission.discodeit.user.domain.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import com.sprint.mission.discodeit.message.application.required.MessageQRepository;
import com.sprint.mission.discodeit.message.domain.Message;
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
      throw new DiscodeitRuntimeException(ErrorCode.INVALID_INFO);
    }
    return message.createdAt.lt(cursor)
        .or(message.createdAt.eq(cursor)
            .and(message.id.lt(idAfter)));
  }
}
