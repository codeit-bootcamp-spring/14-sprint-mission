package com.sprint.mission.discodeit.channel.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.domain.ChannelType;
import com.sprint.mission.discodeit.channel.dto.ChannelDto;
import com.sprint.mission.discodeit.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.channel.repository.ChannelResponseCustom;
import com.sprint.mission.discodeit.user.domain.User;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.channel.domain.QChannel.channel;
import static com.sprint.mission.discodeit.message.domain.QMessage.message;
import static com.sprint.mission.discodeit.readStatus.domain.QReadStatus.readStatus;
import static com.sprint.mission.discodeit.user.domain.QUser.user;

@RequiredArgsConstructor
public class ChannelRepositoryImpl implements ChannelResponseCustom {

    private final ChannelMapper channelMapper;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChannelDto> findAllVisibleTo(UUID userId) {
        JPQLQuery<Instant> lastMessageAt = JPAExpressions
                .select(message.createdAt.max())
                .from(message)
                .where(message.channel.id.eq(channel.id));

        List<Tuple> rows = jpaQueryFactory
                .select(channel, lastMessageAt)
                .from(channel)
                .where(channel.channelType.eq(ChannelType.PUBLIC)
                        .or(channel.id.in(
                                JPAExpressions.select(readStatus.channel.id)
                                        .from(readStatus)
                                        .where(readStatus.user.id.eq(userId)))))
                .fetch();

        if (rows.isEmpty()) {
            return List.of();
        }

        List<UUID> channelIds = rows.stream()
                .map(t -> t.get(channel).getId())
                .toList();

        Map<UUID, List<User>> participantsByChannel = jpaQueryFactory
                .select(readStatus.channel.id, user)
                .from(readStatus)
                .join(readStatus.user, user)
                .leftJoin(user.profile).fetchJoin()
                .leftJoin(user.userStatus).fetchJoin()
                .where(readStatus.channel.id.in(channelIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(
                        t -> t.get(readStatus.channel.id),
                        Collectors.mapping(t -> t.get(user), Collectors.toList())));


        return rows.stream()
                .map(t -> {
                    Channel c = t.get(channel);
                    return channelMapper.toDto(
                            c,
                            participantsByChannel.getOrDefault(c.getId(), List.of()),
                            t.get(lastMessageAt)
                    );
                })
                .toList();
    }


}
